package mg.andrianina.chirp.api.dto

import mg.andrianina.chirp.domain.type.ChatId
import java.time.Instant

data class ChatDto(
    val id: ChatId,
    val participants: List<ChatParticipantDto>,
    val lastActivity: Instant,
    val lastMessage: ChatMessageDto?,
    val creator: ChatParticipantDto
)
