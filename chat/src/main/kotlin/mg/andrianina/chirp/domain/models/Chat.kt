package mg.andrianina.chirp.domain.models

import mg.andrianina.chirp.domain.type.ChatId
import java.time.Instant

data class Chat(
    val id : ChatId,
    val participants: Set<ChatParticipant>,
    val lastMessage: ChatMessage?,
    val creator: ChatParticipant,
    val lastActivity: Instant,
    val createdAt: Instant,
)
