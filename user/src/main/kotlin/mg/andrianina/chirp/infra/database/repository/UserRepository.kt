package mg.andrianina.chirp.infra.database.repository

import mg.andrianina.chirp.domain.model.UserId
import mg.andrianina.chirp.infra.database.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, UserId> {
    fun findByEmail(email: String): UserEntity?
    fun findByEmailOrUsername(email: String, username: String): UserEntity?
}