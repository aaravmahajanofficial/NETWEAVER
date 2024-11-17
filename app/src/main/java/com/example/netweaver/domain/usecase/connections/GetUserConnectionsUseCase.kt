package com.example.netweaver.domain.usecase.connections

import com.example.netweaver.domain.model.Connection
import com.example.netweaver.domain.model.User
import com.example.netweaver.domain.repository.ConnectionType
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.ui.model.Result
import javax.inject.Inject

class GetUserConnectionsUseCase @Inject constructor(private val repository: Repository) {

    suspend fun getConnectionsCount(userId: String): Result<Long> =
        repository.getConnectionsCount(userId = userId)

    suspend fun getPendingConnections(): Result<List<Connection>> =
        repository.getConnectionRequests(
            ConnectionType.IncomingOnly
        )

    suspend fun getRecommendations(): Result<List<User>> = repository.getRecommendations(count = 4)


}