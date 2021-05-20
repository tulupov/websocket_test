package com.example.demo

import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatService {
    fun establishChatSession(chatChannelDTO: ChatChannelDTO): ChatChannelDTO {
        return ChatChannelDTO()
    }

    fun submitMessage(chatChannelDTO: ChatMessageDTO, channelId: UUID): ChatNotification {
        return ChatNotification()
//        return ChatMessageDTO(message = "Hello", senderId = chatChannelDTO.userOne, recipientId = chatChannelDTO.userTwo)
    }
}