package mg.andrianina.chirp.infra.database.mappers

import mg.andrianina.chirp.domain.models.Chat
import mg.andrianina.chirp.domain.models.ChatMessage
import mg.andrianina.chirp.domain.models.ChatParticipant
import mg.andrianina.chirp.infra.database.entities.ChatEntity
import mg.andrianina.chirp.infra.database.entities.ChatParticipantEntity

fun ChatEntity.toChat(lastMessage: ChatMessage? = null): Chat {
    return Chat(
        id = id!!,
        creator = creator.toChatParticipant(),
        participants = participants.map {
            it.toChatParticipant()
        }.toSet(),
        createdAt = createdAt,
        lastMessage = lastMessage,
        lastActivity = lastMessage?.createdAt ?: createdAt,
    )
}

fun ChatParticipantEntity.toChatParticipant(): ChatParticipant {
    return ChatParticipant(
        userId = userId,
        email = email,
        username = username,
        profilePictureUrl = profilePictureUrl
    )
}