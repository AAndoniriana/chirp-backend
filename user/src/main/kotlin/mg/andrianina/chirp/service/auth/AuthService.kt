package mg.andrianina.chirp.service.auth

import mg.andrianina.chirp.domain.exceptions.InvalidCredentialException
import mg.andrianina.chirp.domain.exceptions.UserAlreadyExistException
import mg.andrianina.chirp.domain.exceptions.UserNotFoundException
import mg.andrianina.chirp.domain.model.AuthenticatedUser
import mg.andrianina.chirp.domain.model.User
import mg.andrianina.chirp.domain.model.UserId
import mg.andrianina.chirp.infra.database.entity.RefreshTokenEntity
import mg.andrianina.chirp.infra.database.entity.UserEntity
import mg.andrianina.chirp.infra.database.mappers.toUser
import mg.andrianina.chirp.infra.database.repository.RefreshTokenRepository
import mg.andrianina.chirp.infra.database.repository.UserRepository
import mg.andrianina.chirp.infra.security.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    fun register(
        email: String,
        username: String,
        password: String
    ): User {
        val user = userRepository.findByEmailOrUsername(
            email = email.trim(),
            username = username.trim(),
        )
        if (user != null) {
            throw UserAlreadyExistException()
        }

        val savedUser = userRepository.save(
            UserEntity(
                username = username.trim(),
                email = email.trim(),
                hashedPassword = passwordEncoder.encode(password)
            )
        ).toUser()

        return savedUser
    }

    fun login(email: String, password: String): AuthenticatedUser {
        val user = userRepository.findByEmail(email.trim())
            ?: throw InvalidCredentialException()

        if (!passwordEncoder.matches(password, user.hashedPassword)) {
            throw InvalidCredentialException()
        }

        if (!user.hasVerifiedEmail) {
            //TODO: Check for verified email
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