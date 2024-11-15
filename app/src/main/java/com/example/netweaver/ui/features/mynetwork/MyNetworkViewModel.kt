package com.example.netweaver.ui.features.mynetwork

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netweaver.domain.model.User
import com.example.netweaver.domain.usecase.connections.HandleRequestUseCase
import com.example.netweaver.ui.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyNetworkViewModel @Inject constructor(private val handleRequestUseCase: HandleRequestUseCase) :
    ViewModel() {

    private val _myNetworkUiState = MutableStateFlow(MyNetworkState())
    val myNetworkState: StateFlow<MyNetworkState> = _myNetworkUiState.asStateFlow()

    fun onEvent(event: MyNetworkEvent) {
        when (event) {
            is MyNetworkEvent.Refresh -> {}

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
}

data class MyNetworkState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val users: List<User>? = emptyList<User>(),
    val pendingConnections: List<User>? = emptyList<User>()
)

sealed class MyNetworkEvent {
    object Refresh : MyNetworkEvent()
    data class Accept(val requestId: String) : MyNetworkEvent()
    data class Connect(val userId: String) : MyNetworkEvent()
    data class Ignore(val requestId: String) : MyNetworkEvent()
}