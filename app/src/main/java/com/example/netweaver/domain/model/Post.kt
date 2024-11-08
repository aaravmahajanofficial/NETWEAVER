package com.example.netweaver.domain.model

import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

data class Post(
    val docId: String = "",
    val id: String = "",
    val user: User? = null,
    val content: String = "",
    val mediaUrl: List<String>? = emptyList(),
    val isLiked: Boolean = false,
    val likesCount: Long = 0,
    val commentsCount: Long = 0,
    val createdAt: Instant = now(),
    val updatedAt: Instant = now()
)