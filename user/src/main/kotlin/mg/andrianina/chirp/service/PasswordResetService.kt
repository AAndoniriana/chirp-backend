package mg.andrianina.chirp.service

import mg.andrianina.chirp.domain.events.user.UserEvent
import mg.andrianina.chirp.domain.exceptions.InvalidCredentialException
import mg.andrianina.chirp.domain.exceptions.InvalidTokenException
import mg.andrianina.chirp.domain.exceptions.SamePasswordException
import mg.andrianina.chirp.domain.exceptions.UserNotFoundException
import mg.andrianina.chirp.domain.type.UserId
import mg.andrianina.chirp.infra.database.entity.PasswordResetTokenEntity
import mg.andrianina.chirp.infra.database.repository.PasswordVerificationTokenRepository
import mg.andrianina.chirp.infra.database.repository.RefreshTokenRepository
import mg.andrianina.chirp.infra.database.repository.UserRepository
import mg.andrianina.chirp.infra.message_queue.EventPublisher
import mg.andrianina.chirp.infra.security.PasswordEncoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class PasswordResetService(
    private val passwordResetRepository: PasswordVerificationTokenRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    @param:Value($$"${chirp.email.reset-password.expiry-minutes}") private val expiryMinutes: Long,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val eventPublisher: EventPublisher
) {
    @Transactional
    fun requestPasswordReset(email: String) {
        val user = userRepository.findByEmail(email)
            ?: return

        passwordResetRepository.invalidateActiveTokensForUser(user)

        val token = PasswordResetTokenEntity(
            user = user,
            expiresAt = Instant.now().plus(expiryMinutes, ChronoUnit.MINUTES),
        )
        passwordResetRepository.save(token)

        eventPublisher.publish(
            event = UserEvent.RequestResetPassword(
                userId = user.id!!,
                username = user.username,
                email = user.email,
                verificationToken = token.token,
                expiresInMinutes = expiryMinutes,
            )
        )
    }

    fun resetPassword(token: String, newPassword: String) {
        val passwordResetToken = passwordResetRepository.findByToken(token)
            ?: throw InvalidTokenException("Invalid password reset token")

        if (passwordResetToken.isUsed) {
            throw InvalidTokenException("Password verification token is already used.")
        }

        if (passwordResetToken.isExpired) {
            throw InvalidTokenException("Password verification token has already expired.")
        }

        val user = passwordResetToken.user

        if (passwordEncoder.matches(newPassword, user.hashedPassword)) {
            throw SamePasswordException()
        }

        val hashedPassword = passwordEncoder.encode(newPassword)
        userRepository.save(
            user.apply {
                this.hashedPassword = hashedPassword
            }
        )

        passwordResetRepository.save(
            passwordResetToken.apply {
                this.usedAt = Instant.now()
            }
        )

        refreshTokenRepository.deleteByUserId(user.id!!)
    }

    @Transactional
    fun changePassword(
        userId: UserId,
        oldPassword: String,
        newPassword: String
    ) {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException()

        if (!passwordEncoder.matches(oldPassword, user.hashedPassword)) {
            throw InvalidCredentialException()
        }

        if (newPassword == oldPassword) {
            throw SamePasswordException()
        }

        refreshTokenRepository.deleteByUserId(user.id!!)
        userRepository.save(
            user.apply {
                this.hashedPassword = passwordEncoder.encode(newPassword)
            }
        )
    }

    @Scheduled(cron = "0 0 3 * * * ")
    fun cleanupExpiredTokens() {
        passwordResetRepository.deleteByExpiresAtLessThan(
            now = Instant.now()
        )
    }
}