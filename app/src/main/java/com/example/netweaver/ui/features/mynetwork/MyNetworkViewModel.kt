package com.example.netweaver.ui.features.mynetwork

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netweaver.domain.model.Connection
import com.example.netweaver.domain.model.ConnectionState
import com.example.netweaver.domain.model.User
import com.example.netweaver.domain.usecase.connections.GetUserConnectionsUseCase
import com.example.netweaver.domain.usecase.connections.HandleRequestUseCase
import com.example.netweaver.ui.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyNetworkViewModel @Inject constructor(
    private val handleRequestUseCase: HandleRequestUseCase,
    private val getUserConnectionsUseCase: GetUserConnectionsUseCase
) :
    ViewModel() {

    private val _myNetworkUiState = MutableStateFlow(MyNetworkState())
    val myNetworkState: StateFlow<MyNetworkState> = _myNetworkUiState.asStateFlow()

    init {
        loadPage(isRefreshing = false)
    }

    fun onEvent(event: MyNetworkEvent) {
        when (event) {
            is MyNetworkEvent.Refresh -> {
                loadPage(isRefreshing = true)
            }

            is MyNetworkEvent.Accept -> {
                viewModelScope.launch {

                    _myNetworkUiState.update {
                        it.copy(
                            isLoading = true,
                            error = null
                        )
                    }

                    when (val result =
                        handleRequestUseCase.acceptConnectionRequest(
                            requestId = event.requestId
                        )) {
                        is Result.Success -> {
                            _myNetworkUiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = null,
                                    success = "Successfully accepted the request.",
                                    pendingInvitations = null
                                )
                            }
                        }

                        is Result.Error -> {
                            _myNetworkUiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.exception.message ?: "An unknown error occurred."
                                )
                            }
                        }
                    }


                }
            }

            is MyNetworkEvent.Ignore -> {

                viewModelScope.launch {

                    _myNetworkUiState.update {
                        it.copy(
                            isLoading = true,
                            error = null
                        )
                    }

                    when (val result =
                        handleRequestUseCase.rejectConnectionRequest(
                            requestId = event.requestId
                        )) {
                        is Result.Success -> {
                            _myNetworkUiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = null,
                                    success = "Successfully ignored the request.",
                                    pendingInvitations = null,
                                )
                            }
                        }

                        is Result.Error -> {
                            _myNetworkUiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.exception.message ?: "An unknown error occurred."
                                )
                            }
                        }
                    }


                }


            }

            is MyNetworkEvent.Connect -> {
                viewModelScope.launch {

                    _myNetworkUiState.update {
                        it.copy(
                            isLoading = true,
                            error = null
                        )
                    }

                    when (val result =
                        handleRequestUseCase.sendConnectionRequest(userId = event.userId)) {
                        is Result.Success -> {
                            _myNetworkUiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = null,
                                    success = "Successfully sent the request.",
                                    connectionState = it.connectionState?.plus((event.userId to ConnectionState.PendingOutgoing))
                                )
                            }
                        }

                        is Result.Error -> {
                            _myNetworkUiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.exception.message ?: "An unknown error occurred."
                                )
                            }
                        }
                    }


                }
            }
        }
    }

    private fun loadPage(isRefreshing: Boolean) {

        viewModelScope.launch {

            _myNetworkUiState.update {
                it.copy(
                    isLoading = !isRefreshing,
                    isRefreshing = isRefreshing,
                    error = null
                )
            }

            try {

                val (invitations, recommendations) = coroutineScope {
                    val connectionRequestsDeferred =
                        async { getUserConnectionsUseCase.getPendingConnections() }
                    val recommendationsDeferred =
                        async { getUserConnectionsUseCase.getRecommendations() }
                    Pair(connectionRequestsDeferred.await(), recommendationsDeferred.await())
                }

                when {
                    invitations is Result.Success && recommendations is Result.Success -> {

                        _myNetworkUiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = null,
                                recommendations = recommendations.data,
                                pendingInvitations = invitations.data
                            )
                        }
                    }

                    else -> {

                        handleError(
                            when {
                                invitations is Result.Error -> invitations.exception.message
                                recommendations is Result.Error -> recommendations.exception.message
                                else -> "Unknown Error Occurred"
                            }
                        )
                    }
                }


            } catch (e: Exception) {
                Log.d("ERROR WHILE", e.toString())
                handleError(e.message)
            }
        }

    }

    private fun handleError(error: String?) {

        Log.d("ERROR WHILE", error.toString())

        _myNetworkUiState.update {
            it.copy(
                isLoading = false,
                isRefreshing = false,
                error = error ?: "Unknown error occurred."
            )
        }

    }
}


data class MyNetworkState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val recommendations: List<User>? = emptyList<User>(),
    val pendingInvitations: List<Connection>? = emptyList<Connection>(),
    val connectionState: Map<String, ConnectionState>? = emptyMap()
)

sealed class MyNetworkEvent {
    object Refresh : MyNetworkEvent()
    data class Accept(val requestId: String) : MyNetworkEvent()
    data class Connect(val userId: String) : MyNetworkEvent()
    data class Ignore(val requestId: String) : MyNetworkEvent()
}