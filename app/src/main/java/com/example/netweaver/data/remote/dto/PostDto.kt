package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.Post
import com.example.netweaver.utils.formatTimestampToRelative
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PostDto(
    @SerialName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerialName("user_id")
    val userId: String = "",
    @SerialName("content")
    val content: String = "",
    @SerialName("media_url")
    val mediaUrl: List<String>? = emptyList<String>(),
    @SerialName("likes_count")
    val likesCount: Long = 0L,
    @SerialName("comments_count")
    val commentsCount: Long = 0L,
    @SerialName("created_at")
    val createdAt: Instant = now(),
    @SerialName("updated_at")
    val updatedAt: Instant = now(),
)

fun PostDto.toDomain() = Post(
    id = id,
    content = content,
    mediaUrl = mediaUrl,
    likesCount = likesCount,
    commentsCount = commentsCount,
    createdAt = formatTimestampToRelative(createdAt.toString()),
    updatedAt = formatTimestampToRelative(updatedAt.toString()),
    user = null
)