package mg.andrianina.chirp.service

import mg.andrianina.chirp.domain.exceptions.InvalidTokenException
import mg.andrianina.chirp.domain.exceptions.UserNotFoundException
import mg.andrianina.chirp.domain.model.EmailVerificationToken
import mg.andrianina.chirp.infra.database.entity.EmailVerificationTokenEntity
import mg.andrianina.chirp.infra.database.mappers.toEmailVerification
import mg.andrianina.chirp.infra.database.repository.EmailVerificationTokenRepository
import mg.andrianina.chirp.infra.database.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class EmailVerificationService(
    private val emailVerificationTokenRepository: EmailVerificationTokenRepository,
    private val userRepository: UserRepository,
    @param:Value($$"${chirp.email.verification.expiry-hours}") private val expiryHours: Long
) {
    @Transactional
    fun createVerificationToken(email: String): EmailVerificationToken {
        val userEntity = userRepository.findByEmail(email)
            ?: throw UserNotFoundException()
        emailVerificationTokenRepository.invalidateActiveTokensForUser(userEntity)

        val token = EmailVerificationTokenEntity(
            expiresAt = Instant.now().plus(expiryHours, ChronoUnit.HOURS),
            user = userEntity,
        )

        return emailVerificationTokenRepository.save(token).toEmailVerification()
    }

    @Transactional
    fun verifyEmail(token: String) {
        val verificationToken = emailVerificationTokenRepository.findByToken(token)
            ?: throw InvalidTokenException("Email verification token is invalid.")

        if (verificationToken.isUsed) {
            throw InvalidTokenException("Email verification token is already used.")
        }

        if (verificationToken.isExpired) {
            throw InvalidTokenException("Email verification token has already expired.")
        }

        emailVerificationTokenRepository.save(
            verificationToken.apply {
                this.usedAt = Instant.now()
            }
        )

        userRepository.save(
            verificationToken.user.apply {
                this.hasVerifiedEmail = true
            }
        )
    }

    @Scheduled(cron = "0 0 3 * * *")
    fun cleanupExpiredTokens() {
        emailVerificationTokenRepository.deleteByExpiresAtLessThan(
            now = Instant.now()
        )
    }
}