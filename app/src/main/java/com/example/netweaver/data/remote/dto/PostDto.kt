package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.Post
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    val id: String,
    val userId: String,
    val content: String,
    val mediaUrl: List<String>,
    val likesCount: Int,
    val commentsCount: Int,
    val createdAt: Instant,
    val updatedAt: Instant
)

fun PostDto.toDomain() = Post(
    id = id,
    userId = userId,
    content = content,
    mediaUrl = mediaUrl,
    likesCount = likesCount,
    commentsCount = commentsCount,
    createdAt = createdAt,
    updatedAt = updatedAt,
    user = null
)