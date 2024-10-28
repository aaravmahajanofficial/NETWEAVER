package com.example.netweaver.domain.model

import kotlinx.datetime.Instant

data class Like(
    val id: String,
    val userId: String,
    val postId: String,
    val createdAt: Instant,
    val user: User? = null
)
