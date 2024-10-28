package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.Connection
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConnectionDto(
    val id: String,
    val requesterId: String,
    val receiverId: String,
    val status: ConnectionStatus,
    val createdAt: Instant,
    val updatedAt: Instant
)

@Serializable
enum class ConnectionStatus {
    @SerialName("pending")
    PENDING,

    @SerialName("accepted")
    ACCEPTED,

    @SerialName("rejected")
    REJECTED
}

fun ConnectionDto.toDomainModel() = Connection(
    id = id,
    requesterId = requesterId,
    receiverId = receiverId,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt
)
