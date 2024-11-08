package com.example.netweaver.data.remote.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class LikeDto(
    @SerialName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerialName("user_id")
    val userId: String,
    @SerialName("post_id")
    val postId: String,
    @SerialName("created_at")
    val createdAt: Instant
)