package mg.andrianina.chirp.infra.database.repository

import mg.andrianina.chirp.domain.model.UserId
import mg.andrianina.chirp.infra.database.entity.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository: JpaRepository<RefreshTokenEntity, Long> {
    fun findByUserIdAndHashedToken(userId: UserId, hashedToken: String): RefreshTokenEntity?
    fun deleteByUserIdAndHashedToken(userId: UserId, hashedToken: String)
    fun deleteByUserId(userId: UserId)
}