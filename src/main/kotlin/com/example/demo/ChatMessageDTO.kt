package com.example.demo

import java.util.*

data class ChatMessageDTO(val message: String = "", val senderId: UUID = UUID.randomUUID(), val senderName: String = "", val recipientName: String = "", val recipientId: UUID = UUID.randomUUID())
