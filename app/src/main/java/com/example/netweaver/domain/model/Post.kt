package com.example.netweaver.domain.model

data class Post(
    val id: String = "",
    val user: User? = null,
    val content: String = "",
    val mediaUrl: List<String>? = emptyList(),
    val isLiked: Boolean = false,
    val likesCount: Long = 0,
    val commentsCount: Long = 0,
    val createdAt: String,
    val updatedAt: String,
)