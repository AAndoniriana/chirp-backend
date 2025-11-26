package mg.andrianina.chirp.infra.database.repositories

import mg.andrianina.chirp.domain.type.ChatId
import mg.andrianina.chirp.domain.type.UserId
import mg.andrianina.chirp.infra.database.entities.ChatEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ChatRepository: JpaRepository<ChatEntity, ChatId> {
    @Query("""
        SELECT c 
        FROM ChatEntity c 
        LEFT JOIN FETCH c.participants
        LEFT JOIN FETCH c.creator
        WHERE c.id = :id
        AND EXISTS(
            SELECT 1 
            FROM c.participants p
            WHERE p.userId = :userId
        )
    """)
    fun findChatById(id: ChatId, userId: UserId): ChatEntity?

    @Query("""
        SELECT c 
        FROM ChatEntity c 
        LEFT JOIN FETCH c.participants
        LEFT JOIN FETCH c.creator
        WHERE EXISTS(
            SELECT 1
            FROM c.participants p
            WHERE p.userId = :userId
        )
    """)
    fun findAllByUserId(userId: UserId): List<ChatEntity>
}