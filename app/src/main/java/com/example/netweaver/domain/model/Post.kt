package com.example.netweaver.domain.model

import kotlinx.datetime.Instant

data class Post(
    val id: String,
    val userId: String,
    val content: String,
    val mediaUrl: List<String> = emptyList(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val createdAt: Instant,
    val updatedAt: Instant,
    val user: User? = null
)