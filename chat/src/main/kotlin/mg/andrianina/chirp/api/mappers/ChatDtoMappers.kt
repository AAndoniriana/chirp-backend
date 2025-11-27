package mg.andrianina.chirp.api.mappers

import mg.andrianina.chirp.api.dto.ChatDto
import mg.andrianina.chirp.api.dto.ChatMessageDto
import mg.andrianina.chirp.api.dto.ChatParticipantDto
import mg.andrianina.chirp.domain.models.Chat
import mg.andrianina.chirp.domain.models.ChatMessage
import mg.andrianina.chirp.domain.models.ChatParticipant

fun Chat.toChatDto(): ChatDto {
    return ChatDto(
        id = id,
        creator = creator.toChatParticipantDto(),
        participants = participants.map {
            it.toChatParticipantDto()
        },
        lastMessage = lastMessage?.toChatMessageDto(),
        lastActivity = lastActivity,
    )
}

fun ChatMessage.toChatMessageDto(): ChatMessageDto {
    return ChatMessageDto(
        id = id,
        chatId = chatId,
        content = content,
        createdAt = createdAt,
        senderId = sender.userId
    )
}

fun ChatParticipant.toChatParticipantDto(): ChatParticipantDto {
    return ChatParticipantDto(
        userId = userId,
        username = username,
        email = email,
        profilePictureUrl = profilePictureUrl
    )
}