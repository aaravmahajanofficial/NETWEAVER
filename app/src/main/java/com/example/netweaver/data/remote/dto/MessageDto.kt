package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.Message
import com.example.netweaver.utils.formatTimestampToRelative
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    @SerialName("id")
    val id: String,
    @SerialName("chat_id")
    val chatId: String,
    @SerialName("sender_id")
    val senderId: String,
    @SerialName("content")
    val content: String,
    @SerialName("created_at")
    val createdAt: Instant = now()
)

fun MessageDto.toDomain() = Message(
    id = id,
    chatId = chatId,
    senderId = senderId,
    content = content,
    createdAt = formatTimestampToRelative(createdAt.toString())
)