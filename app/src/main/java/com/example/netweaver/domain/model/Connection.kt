package com.example.netweaver.domain.model

import com.example.netweaver.data.remote.dto.ConnectionStatus
import kotlinx.datetime.Instant

data class Connection(
    val id: String,
    val requesterId: String,
    val receiverId: String,
    val status: ConnectionStatus,
    val createdAt: Instant,
    val updatedAt: Instant
)

