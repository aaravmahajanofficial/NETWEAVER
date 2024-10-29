package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.Post
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("content")
    val content: String,
    @SerialName("media_url")
    val mediaUrl: List<String>,
    @SerialName("likes_count")
    val likesCount: Int,
    @SerialName("comments_count")
    val commentsCount: Int,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant,
    val user: UserDto? = null,
)

fun PostDto.toDomain() = Post(
    id = id,
    content = content,
    mediaUrl = mediaUrl,
    likesCount = likesCount,
    commentsCount = commentsCount,
    createdAt = createdAt,
    updatedAt = updatedAt,
    user = null
)