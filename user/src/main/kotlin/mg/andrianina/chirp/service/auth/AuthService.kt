package mg.andrianina.chirp.service.auth

import mg.andrianina.chirp.domain.exceptions.UserAlreadyExistException
import mg.andrianina.chirp.domain.model.User
import mg.andrianina.chirp.infra.database.entity.UserEntity
import mg.andrianina.chirp.infra.database.mappers.toUser
import mg.andrianina.chirp.infra.database.repository.UserRepository
import mg.andrianina.chirp.infra.security.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun register(email: String, username: String, password: String): User {
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
}