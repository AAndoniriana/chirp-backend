package mg.andrianina.chirp.api.dto

import mg.andrianina.chirp.domain.type.ChatId
import mg.andrianina.chirp.domain.type.ChatMessageId
import mg.andrianina.chirp.domain.type.UserId
import java.time.Instant

data class ChatMessageDto(
    val id: ChatMessageId,
    val chatId: ChatId,
    val content: String,
    val createdAt: Instant,
    val senderId: UserId,
)
