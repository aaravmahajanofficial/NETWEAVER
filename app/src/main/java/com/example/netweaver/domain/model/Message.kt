package com.example.netweaver.domain.model

data class Message(
    val id: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val content: String = "",
    val createdAt: String = "",
    val fromCurrentUser: Boolean = false
)