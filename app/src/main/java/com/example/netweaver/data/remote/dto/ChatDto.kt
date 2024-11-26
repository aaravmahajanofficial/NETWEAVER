package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.Chat
import com.example.netweaver.utils.formatTimestampToRelative
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    @SerialName("id")
    val id: String,
    @SerialName("created_at")
    val createdAt: Instant = now()
)

fun ChatDto.toDomain() = Chat(
    id = id,
    createdAt = formatTimestampToRelative(createdAt.toString())
)