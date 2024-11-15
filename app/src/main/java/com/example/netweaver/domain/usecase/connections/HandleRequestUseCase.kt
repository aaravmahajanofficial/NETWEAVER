package com.example.netweaver.domain.usecase.connections

import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.ui.model.Result
import javax.inject.Inject

class HandleRequestUseCase @Inject constructor(private val repository: Repository) {

    suspend fun sendConnectionRequest(userId: String): Result<Unit> =
        repository.sendConnectionRequest(userId = userId)

    suspend fun acceptConnectionRequest(requestId: String): Result<Unit> =
        repository.acceptConnectionRequest(requestId)

    suspend fun rejectConnectionRequest(requestId: String): Result<Unit> =
        repository.rejectConnectionRequest(requestId)

}