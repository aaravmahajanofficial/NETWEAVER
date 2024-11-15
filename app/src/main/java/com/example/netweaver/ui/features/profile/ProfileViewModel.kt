package com.example.netweaver.ui.features.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netweaver.domain.model.Connection
import com.example.netweaver.domain.model.ConnectionState
import com.example.netweaver.domain.model.Education
import com.example.netweaver.domain.model.Experience
import com.example.netweaver.domain.model.Post
import com.example.netweaver.domain.model.User
import com.example.netweaver.domain.usecase.connections.GetConnectionStatusUseCase
import com.example.netweaver.domain.usecase.connections.GetUserConnectionsUseCase
import com.example.netweaver.domain.usecase.connections.HandleRequestUseCase
import com.example.netweaver.domain.usecase.user.GetEducationUseCase
import com.example.netweaver.domain.usecase.user.GetExperiencesUseCase
import com.example.netweaver.domain.usecase.user.GetUserPostsUseCase
import com.example.netweaver.domain.usecase.user.UserProfileUseCase
import com.example.netweaver.ui.model.Result
import com.google.firebase.auth.FirebaseAuth
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
class ProfileViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase,
    private val getEducationUseCase: GetEducationUseCase,
    private val getExperiencesUseCase: GetExperiencesUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getConnectionStatusUseCase: GetConnectionStatusUseCase,
    private val getUserConnectionsUseCase: GetUserConnectionsUseCase,
    private val handleRequestUseCase: HandleRequestUseCase,
    firebaseAuth: FirebaseAuth,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId = savedStateHandle.get<String>("userId")
    private val currentUserId =
        firebaseAuth.currentUser?.uid ?: "0f07b4e0-4eb8-4a9a-be40-07ae8f608b0e"

    private val _profileUiState = MutableStateFlow(ProfileState())
    val profileUiState: StateFlow<ProfileState> = _profileUiState.asStateFlow()

    private val _profileType = MutableStateFlow<ProfileType>(ProfileType.PersonalProfile)
    val profileType: StateFlow<ProfileType> = _profileType.asStateFlow()

    init {

        if (userId == currentUserId) {
            _profileType.value = ProfileType.PersonalProfile
        } else {
            _profileType.value = ProfileType.OtherProfile
        }

        loadProfile(isRefreshing = false)

    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.Refresh -> {
                loadProfile(isRefreshing = true)
            }

            is ProfileEvent.Accept -> {
                viewModelScope.launch {

                    _profileUiState.update {
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
                            _profileUiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = null,
                                    success = "Successfully accepted the request.",
                                    connectionState = ConnectionState.Connected
                                )
                            }
                        }

                        is Result.Error -> {
                            _profileUiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.exception.message ?: "An unknown error occurred."
                                )
                            }
                        }
                    }


                }
            }

            is ProfileEvent.Ignore -> {

                viewModelScope.launch {

                    _profileUiState.update {
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
                            _profileUiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = null,
                                    success = "Successfully ignored the request.",
                                    connectionState = null
                                )
                            }
                        }

                        is Result.Error -> {
                            _profileUiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.exception.message ?: "An unknown error occurred."
                                )
                            }
                        }
                    }


                }


            }

            is ProfileEvent.Connect -> {
                viewModelScope.launch {

                    _profileUiState.update {
                        it.copy(
                            isLoading = true,
                            error = null
                        )
                    }

                    when (val result =
                        handleRequestUseCase.sendConnectionRequest(userId = event.userId)) {
                        is Result.Success -> {
                            _profileUiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = null,
                                    success = "Successfully sent the request.",
                                    connectionState = ConnectionState.PendingOutgoing
                                )
                            }
                        }

                        is Result.Error -> {
                            _profileUiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.exception.message ?: "An unknown error occurred."
                                )
                            }
                        }
                    }


                }
            }

            is ProfileEvent.Message -> {}
        }
    }

    private fun loadProfile(isRefreshing: Boolean) {

        viewModelScope.launch {

            _profileUiState.update {
                it.copy(
                    isLoading = !isRefreshing,
                    error = null,
                    isRefreshing = isRefreshing
                )
            }

            try {
                // 1. parallel loading
                // 2. for fast failing, if any one stops others are also cancelled -> Atomic Transaction (All or null)
                coroutineScope {
                    val profileDeferred = async { userProfileUseCase(userId = userId!!) }
                    val postsDeferred = async { getUserPostsUseCase(userId = userId!!) }
                    val educationDeferred = async { getEducationUseCase(userId = userId!!) }
                    val experienceDeferred = async { getExperiencesUseCase(userId = userId!!) }
                    val connectionsCountDeferred =
                        async { getUserConnectionsUseCase.getConnectionsCount(userId = userId!!) }
                    val connectionDeferred = if (userId != currentUserId) {
                        async { getConnectionStatusUseCase(userId = userId!!) }
                    } else null

                    // wait for all to complete
                    val userDetails = when (val result = profileDeferred.await()) {
                        is Result.Success -> result.data
                        is Result.Error -> throw result.exception
                    }

                    val posts = try {
                        when (val result = postsDeferred.await()) {
                            is Result.Success -> result.data
                            is Result.Error -> null
                        }
                    } catch (_: Exception) {
                        null
                    }

                    val education = try {
                        when (val result = educationDeferred.await()) {
                            is Result.Success -> result.data
                            is Result.Error -> null
                        }
                    } catch (_: Exception) {
                        null
                    }

                    val experience = try {
                        when (val result = experienceDeferred.await()) {
                            is Result.Success -> result.data
                            is Result.Error -> null
                        }

                    } catch (_: Exception) {
                        null
                    }

                    val connection = try {

                        when (val result = connectionDeferred?.await()) {

                            is Result.Success -> result.data
                            is Result.Error -> null
                            null -> null
                        }

                    } catch (_: Exception) {
                        null
                    }

                    val connectionsCount = try {

                        when (val result = connectionsCountDeferred.await()) {

                            is Result.Success -> result.data
                            is Result.Error -> null
                        }

                    } catch (_: Exception) {
                        null
                    }

                    _profileUiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            user = userDetails,
                            posts = posts,
                            education = education,
                            experience = experience,
                            connection = connection,
                            connectionsCount = connectionsCount,
                            connectionState = connection?.getConnectionState(currentUserId),
                            error = null
                        )
                    }

                }

            } catch (e: Exception) {
                _profileUiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = e.message ?: "Unknown error occurred"
                    )
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

    val user: User? = null,
    val posts: List<Post>? = emptyList<Post>(),
    val education: List<Education>? = emptyList<Education>(),
    val experience: List<Experience>? = emptyList<Experience>(),

    val connectionsCount: Long? = 0,
    val connection: Connection? = null,
    val connectionState: ConnectionState? = null
)

sealed class ProfileType {
    data object PersonalProfile : ProfileType()
    data object OtherProfile : ProfileType()
}

sealed class ProfileEvent {
    object Refresh : ProfileEvent()
    data class Accept(val requestId: String) : ProfileEvent()
    data class Connect(val userId: String) : ProfileEvent()
    data class Ignore(val requestId: String) : ProfileEvent()
    object Message : ProfileEvent()
}