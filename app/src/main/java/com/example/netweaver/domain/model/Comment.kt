package com.example.netweaver.domain.model

import kotlinx.datetime.Instant

data class Comment(
    val id: String,
    val userId: String,
    val postId: String,
    val content: String,
    val parentCommentId: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant,
    val user: User? = null
)