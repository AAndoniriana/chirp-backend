package mg.andrianina.chirp.api.controller

import jakarta.validation.Valid
import mg.andrianina.chirp.api.dto.ChatDto
import mg.andrianina.chirp.api.dto.CreateChatRequest
import mg.andrianina.chirp.api.mappers.toChatDto
import mg.andrianina.chirp.api.util.requestUserId
import mg.andrianina.chirp.service.ChatService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class ChatController(private val chatService: ChatService) {
    @PostMapping
    fun createChat(
        @Valid @RequestBody body: CreateChatRequest
    ): ChatDto {
        return chatService.createChat(
            creatorId = requestUserId,
            otherUserIds = body.otherUserIds.toSet()
        ).toChatDto()
    }
}