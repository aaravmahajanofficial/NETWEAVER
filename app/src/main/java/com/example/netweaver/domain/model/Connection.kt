package com.example.netweaver.domain.model

import com.example.netweaver.data.remote.dto.ConnectionStatus
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

data class Connection(
    val id: String = "",
    val requesterId: String = "",
    val receiverId: String = "",
    val status: ConnectionStatus? = null,
    val createdAt: Instant = now(),
    val updatedAt: Instant = now(),
    val user: User? = null
) {
    fun getConnectionState(currentUserId: String): ConnectionState? =
        when {
            status == ConnectionStatus.PENDING && requesterId == currentUserId -> {
                ConnectionState.PendingOutgoing
            }

            status == ConnectionStatus.PENDING && receiverId == currentUserId -> {
                ConnectionState.PendingIncoming
            }

            status == ConnectionStatus.CONNECTED -> {
                ConnectionState.Connected
            }

            else -> {
                null
            }
        }
}

sealed interface ConnectionState {
    object PendingOutgoing : ConnectionState
    object PendingIncoming : ConnectionState
    object Connected : ConnectionState
}
