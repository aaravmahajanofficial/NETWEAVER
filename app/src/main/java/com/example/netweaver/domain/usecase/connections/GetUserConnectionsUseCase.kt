package com.example.netweaver.domain.usecase.connections

import com.example.netweaver.domain.model.User
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.ui.model.Result
import javax.inject.Inject

class GetUserConnectionsUseCase @Inject constructor(private val repository: Repository) {

    suspend fun getConnectionsCount(userId: String): Result<Long> =
        repository.getConnectionsCount(userId = userId)

    suspend fun getConnections(userId: String): Result<List<User>> =
        repository.getConnections(userId = userId)

    suspend fun pendingConnections(userId: String): Result<List<User>> =
        repository.getPendingConnections(userId = userId)
}