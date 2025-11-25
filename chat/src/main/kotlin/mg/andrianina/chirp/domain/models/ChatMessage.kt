package mg.andrianina.chirp.domain.models

import mg.andrianina.chirp.domain.type.ChatId
import mg.andrianina.chirp.domain.type.ChatMessageId
import java.time.Instant

data class ChatMessage(
    val id: ChatMessageId,
    val chatId: ChatId,
    val sender: ChatParticipant,
    val recipient: ChatParticipant,
    val content: String,
    val createdAt: Instant,
)
