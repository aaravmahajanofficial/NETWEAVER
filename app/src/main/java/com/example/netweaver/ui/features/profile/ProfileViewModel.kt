package com.example.netweaver.ui.features.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netweaver.domain.model.User
import com.example.netweaver.domain.usecase.user.UserProfileUseCase
import com.example.netweaver.ui.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId = savedStateHandle.get<String>("userId")

    private val _profileUiState = MutableStateFlow(ProfileState())
    val profileUiState: StateFlow<ProfileState> = _profileUiState.asStateFlow()

    init {
        loadProfile(isRefreshing = false)
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.Refresh -> {
                loadProfile(isRefreshing = true)
            }
        }
    }

    private fun loadProfile(isRefreshing: Boolean) {

        if (userId.isNullOrBlank()) {
            _profileUiState.update { it.copy(error = "Invalid User Id") }
            return
        }

        viewModelScope.launch {

            _profileUiState.update {
                it.copy(
                    isLoading = !isRefreshing,
                    error = null,
                    isRefreshing = isRefreshing
                )
            }

            when (val result = userProfileUseCase(userId = userId)) {
                is Result.Success -> {
                    _profileUiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            user = result.data,
                            error = null
                        )
                    }
                }

                is Result.Error -> {
                    _profileUiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.exception.message ?: "Unknown error occurred"
                        )
                    }

                }
            }

        }

    }


}

data class ProfileState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val user: User? = null
)

sealed class ProfileEvent {
    object Refresh : ProfileEvent()
}