package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.Connection
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConnectionDto(
    @SerialName("id")
    val id: String = "",
    @SerialName("requester_id")
    val requesterId: String = "",
    @SerialName("receiver_id")
    val receiverId: String = "",
    @SerialName("status")
    val status: ConnectionStatus? = null,
    @SerialName("created_at")
    val createdAt: Instant = now(),
    @SerialName("updated_at")
    val updatedAt: Instant = now()
)

@Serializable
enum class ConnectionStatus {
    PENDING,
    CONNECTED,
}

fun ConnectionDto.toDomainModel() = Connection(
    id = id,
    requesterId = requesterId,
    receiverId = receiverId,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt
)
