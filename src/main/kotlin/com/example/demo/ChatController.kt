package com.example.demo

import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

@Controller
class ChatController(private val chatService: ChatService) {

    @MessageMapping("/user/chat/{channelId}")
    @SendTo("/topic/chat/{channelId}")
    fun chatMessage(@DestinationVariable("channelId") channelId: UUID, chatMessageDTO: ChatMessageDTO): ChatNotification {
        return chatService.submitMessage(chatMessageDTO, channelId)
    }
}