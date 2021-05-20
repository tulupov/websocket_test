package com.example.demo

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.lang.Nullable
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.WebSocketTransport
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatControllerIT(@Autowired private val chatService: ChatService) {

    @LocalServerPort
    var port = 0

    var completableFuture: CompletableFuture<ChatNotification> = CompletableFuture()
    lateinit var webSocketStompClient: WebSocketStompClient

    @BeforeEach
    fun setup() {
        this.webSocketStompClient = WebSocketStompClient(SockJsClient(listOf(WebSocketTransport(StandardWebSocketClient()))))
        webSocketStompClient.messageConverter = MappingJackson2MessageConverter()
    }

    @Test
    fun verifyGreetingIsReceived() {
        val channel = chatService.establishChatSession(ChatChannelDTO(userOne = UUID.randomUUID(), userTwo = UUID.randomUUID()))
        val stompSession = webSocketStompClient.connect("ws://localhost:$port/ws", object : StompSessionHandlerAdapter() {}).get(10, TimeUnit.SECONDS)
        println("subscribing to::::::::::   /topic/chat/${channel.channelId}")

        val message = ChatMessageDTO(message = "Hello", senderId = channel.userOne, senderName = "Pranav", recipientName = "Monika", recipientId = channel.userTwo)
        stompSession.send("/api/user/chat/${channel.channelId}", message)

        stompSession.subscribe("/topic/chat/${channel.channelId}", object: StompFrameHandler {

            override fun getPayloadType(headers: StompHeaders): Type {
                return ChatNotification::class.java
            }

            override fun handleFrame(headers: StompHeaders, @Nullable payload: Any?) {
                completableFuture.complete(payload as ChatNotification)
            }
        })

        val response = completableFuture.get(10, TimeUnit.SECONDS)
        println(response)
    }
}