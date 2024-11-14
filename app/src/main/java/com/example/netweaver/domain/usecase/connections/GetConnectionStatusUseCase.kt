package com.example.netweaver.domain.usecase.connections

import com.example.netweaver.domain.model.Connection
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.ui.model.Result
import javax.inject.Inject

class GetConnectionStatusUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(userId: String): Result<Connection> =
        repository.getConnectionStatus(userId = userId)

}