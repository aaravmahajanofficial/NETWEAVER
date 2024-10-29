package com.example.netweaver.ui.features.profile

import androidx.compose.runtime.Immutable
import com.example.netweaver.domain.model.User

@Immutable
data class ProfileUiState(
    val user: User? = null,
    val isConnected: Boolean,
    val mutualConnections: Int = 0
)