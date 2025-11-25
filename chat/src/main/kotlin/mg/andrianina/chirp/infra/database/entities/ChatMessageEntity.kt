package mg.andrianina.chirp.infra.database.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import mg.andrianina.chirp.domain.type.ChatId
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(
    schema = "chat_service",
    name = "chats",
    indexes = [
        Index(
            name = "idx_chat_message_chat_id_created_at",
            columnList = "chat_id,created_at DESC"
        )
    ]
)
class ChatMessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: ChatId? = null,
    @Column(nullable = false)
    var content: String,
    @Column(
        name = "chat_id",
        nullable = false,
        updatable = false
    )
    var chatId: ChatId,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "chat_id",
        nullable = false,
        updatable = false,
        insertable = false
    )
    var chat: ChatEntity? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "sender_id",
        nullable = false,
        updatable = false,
        insertable = false
    )
    var sender: ChatParticipantsEntity? = null,
    @CreationTimestamp
    var createdAt: Instant = Instant.now(),
)