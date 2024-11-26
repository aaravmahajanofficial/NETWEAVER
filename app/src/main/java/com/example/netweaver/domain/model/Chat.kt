package com.example.netweaver.domain.model

data class Chat(
    val id: String = "",
    val participant: User? = null,
    val createdAt: String = ""
)