package mg.andrianina.chirp.service

import mg.andrianina.chirp.domain.events.user.UserEvent
import mg.andrianina.chirp.domain.exceptions.EmailNotVerifiedException
import mg.andrianina.chirp.domain.exceptions.InvalidCredentialException
import mg.andrianina.chirp.domain.exceptions.InvalidTokenException
import mg.andrianina.chirp.domain.exceptions.UserAlreadyExistException
import mg.andrianina.chirp.domain.exceptions.UserNotFoundException
import mg.andrianina.chirp.domain.model.AuthenticatedUser
import mg.andrianina.chirp.domain.model.User
import mg.andrianina.chirp.domain.type.UserId
import mg.andrianina.chirp.infra.database.entity.RefreshTokenEntity
import mg.andrianina.chirp.infra.database.entity.UserEntity
import mg.andrianina.chirp.infra.database.mappers.toUser
import mg.andrianina.chirp.infra.database.repository.RefreshTokenRepository
import mg.andrianina.chirp.infra.database.repository.UserRepository
import mg.andrianina.chirp.infra.message_queue.EventPublisher
import mg.andrianina.chirp.infra.security.PasswordEncoder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val emailVerificationService: EmailVerificationService,
    private val eventPublisher: EventPublisher
) {
    @Transactional
    fun register(
        email: String,
        username: String,
        password: String
    ): User {
        val trimmedEmail = email.trim()
        val user = userRepository.findByEmailOrUsername(
            email = trimmedEmail,
            username = username.trim(),
        )
        if (user != null) {
            throw UserAlreadyExistException()
        }

        val savedUser = userRepository.saveAndFlush(
            UserEntity(
                username = username.trim(),
                email = email.trim(),
                hashedPassword = passwordEncoder.encode(password)
            )
        ).toUser()

        val token = emailVerificationService.createVerificationToken(trimmedEmail)

        eventPublisher.publish(
            UserEvent.Created(
                userId = savedUser.id,
                email = savedUser.email,
                username = savedUser.username,
                verificationToken = token.token,
            )
        )

        return savedUser
    }

    fun login(email: String, password: String): AuthenticatedUser {
        val user = userRepository.findByEmail(email.trim())
            ?: throw InvalidCredentialException()

        if (!passwordEncoder.matches(password, user.hashedPassword)) {
            throw InvalidCredentialException()
        }

        if (!user.hasVerifiedEmail) {
            throw EmailNotVerifiedException()
        }

        return  user.id?.let {
            val accessToken = jwtService.generateToken(it)
            val refreshToken = jwtService.generateRefreshToken(it)

            storeRefreshToken(it, refreshToken)

            AuthenticatedUser(
                user = user.toUser(),
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        } ?: throw UserNotFoundException()
    }

    @Transactional
    fun refreshToken(refreshToken: String): AuthenticatedUser {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw InvalidTokenException(
                message = "Invalid refresh token"
            )
        }

        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException()

        val hashed = hashToken(refreshToken)

        return user.id?.let {
            refreshTokenRepository.findByUserIdAndHashedToken(
                userId = it,
                hashedToken = hashed
            ) ?: throw InvalidTokenException("Invalid refresh token")

            refreshTokenRepository.deleteByUserIdAndHashedToken(
                userId = it,
                hashedToken = hashed
            )

            storeRefreshToken(it, refreshToken)

            val newAccessToken = jwtService.generateToken(it)
            val newRefreshToken = jwtService.generateRefreshToken(it)

            AuthenticatedUser(
                user = user.toUser(),
                accessToken = newAccessToken,
                refreshToken = newRefreshToken
            )
        } ?: throw UserNotFoundException()
    }

    @Transactional
    fun logout(refreshToken: String) {
        val userId = jwtService.getUserIdFromToken(refreshToken)
        val hashedToken = hashToken(refreshToken)
        refreshTokenRepository.deleteByUserIdAndHashedToken(userId, hashedToken)
    }

    private fun storeRefreshToken(userId: UserId, token: String) {
        val hashedToken = hashToken(token)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiresAt = Instant.now().plusMillis(expiryMs)

        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashedToken,
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.toByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}