package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.Like
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class LikeDto(
    val id: String,
    val userId: String,
    val postId: String,
    val createdAt: Instant
)

fun LikeDto.toDomain() = Like(
    id = id,
    userId = userId,
    postId = postId,
    createdAt = createdAt,
    user = null
)