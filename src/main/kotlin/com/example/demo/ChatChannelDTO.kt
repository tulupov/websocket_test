package com.example.demo

import java.util.*

data class ChatChannelDTO(val userOne: UUID = UUID.randomUUID(), val userTwo: UUID = UUID.randomUUID(), val channelId: UUID = UUID.randomUUID())
