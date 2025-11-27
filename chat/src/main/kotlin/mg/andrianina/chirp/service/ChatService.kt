package mg.andrianina.chirp.service

import mg.andrianina.chirp.domain.models.Chat
import mg.andrianina.chirp.domain.type.UserId
import mg.andrianina.chirp.exception.ChatParticipantNotFoundException
import mg.andrianina.chirp.exception.InvalidChatSizeException
import mg.andrianina.chirp.infra.database.entities.ChatEntity
import mg.andrianina.chirp.infra.database.mappers.toChat
import mg.andrianina.chirp.infra.database.repositories.ChatParticipantRepository
import mg.andrianina.chirp.infra.database.repositories.ChatRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val chatParticipantRepository: ChatParticipantRepository
) {
    @Transactional
    fun createChat(
        creatorId: UserId,
        otherUserIds: Set<UserId>
    ): Chat {
        val otherParticipant = chatParticipantRepository.findByUserIdIn(
            userIds = otherUserIds
        )
        if ((otherParticipant + creatorId).size < 2) {
            throw InvalidChatSizeException()
        }

        val creator = chatParticipantRepository.findByIdOrNull(creatorId)
            ?: throw ChatParticipantNotFoundException(creatorId)

        return chatRepository.save(
            ChatEntity(
                creator = creator,
                participants = (otherParticipant + creator).toMutableSet(),
            )
        ).toChat()
    }
}