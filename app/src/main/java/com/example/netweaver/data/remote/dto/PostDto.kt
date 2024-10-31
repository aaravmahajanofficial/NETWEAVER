package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.Post
import com.google.firebase.Timestamp
import kotlinx.datetime.Instant

data class PostDto(
    val id: String = "",
    val userId: String = "",
    val content: String? = "",
    val mediaUrl: List<String>? = emptyList<String>(),
    val likesCount: Long? = 0L,
    val commentsCount: Long? = 0L,
    val createdAt: Timestamp? = Timestamp.now(),
    val updatedAt: Timestamp? = Timestamp.now()
)

fun PostDto.toDomain() = Post(
    id = id,
    content = content,
    mediaUrl = mediaUrl,
    likesCount = likesCount,
    commentsCount = commentsCount,
    createdAt = createdAt?.toDate()?.time?.let { Instant.fromEpochMilliseconds(it) },
    updatedAt = updatedAt?.toDate()?.time?.let { Instant.fromEpochMilliseconds(it) },
    user = null
)