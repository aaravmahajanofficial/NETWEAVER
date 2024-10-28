package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.Comment
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CommentDto(
    val id: String,
    val userId: String,
    val postId: String,
    val content: String,
    val parentCommentId: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant
)

fun CommentDto.toDomainModel() = Comment(
    id = id,
    userId = userId,
    postId = postId,
    content = content,
    parentCommentId = parentCommentId,
    createdAt = createdAt,
    updatedAt = updatedAt,
    user = null
)