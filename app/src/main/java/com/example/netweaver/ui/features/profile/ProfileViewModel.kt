package com.example.netweaver.ui.features.profile

import android.util.Log
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
        if (validateUserId()) {

            if (userId == currentUserId) {
                _profileType.value = ProfileType.PersonalProfile
            } else {
                _profileType.value = ProfileType.OtherProfile
            }

            loadProfile(isRefreshing = false)
        }
    }

    fun validateUserId(): Boolean {

        return if (userId.isNullOrBlank()) {
            _profileUiState.update { it.copy(error = "Invalid User Id") }
            false
        } else
            true
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.Refresh -> {
                loadProfile(isRefreshing = true)
            }
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
                    val connectionDeferred = if (userId != currentUserId) {
                        async { getConnectionStatusUseCase(userId = userId!!) }
                    } else null

                    // wait for all to complete
                    val user = when (val result = profileDeferred.await()) {
                        is Result.Success -> result.data
                        is Result.Error -> throw result.exception // Ultimate Failure
                    }

                    val posts = try {
                        when (val result = postsDeferred.await()) {
                            is Result.Success -> result.data
                            is Result.Error -> {
                                Log.d("ERROR 1", result.exception.message.toString())
                                null
                            }
                        }
                    } catch (_: Exception) {
                        null
                    }

                    val education = try {
                        when (val result = educationDeferred.await()) {
                            is Result.Success -> result.data
                            is Result.Error -> {
                                Log.d("ERROR 2", result.exception.message.toString())
                                null
                            }
                        }
                    } catch (_: Exception) {
                        null
                    }

                    val experience = try {
                        when (val result = experienceDeferred.await()) {
                            is Result.Success -> result.data
                            is Result.Error -> {
                                Log.d("ERROR 3", result.exception.message.toString())
                                null
                            }
                        }

                    } catch (_: Exception) {
                        null
                    }

                    val connection = try {

                        when (val result = connectionDeferred?.await()) {

                            is Result.Success -> result.data
                            is Result.Error -> {
                                Log.d("ERROR 4", result.exception.message.toString())
                                null
                            }

                            null -> null
                        }

                    } catch (_: Exception) {
                        null
                    }

                    _profileUiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            user = user,
                            posts = posts,
                            education = education,
                            experience = experience,
                            connection = connection,
                            connectionState = connection?.getConnectionState(userId!!)!!,
                            error = null
                        )
                    }

                }

            } catch (e: Exception) {
                Log.d("ERROR 5", e.message.toString())
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
    val posts: List<Post>? = null,
    val education: List<Education>? = null,
    val experience: List<Experience>? = null,

    val connection: Connection? = null,
    val connectionState: ConnectionState = ConnectionState.None
)

sealed class ProfileType {
    data object PersonalProfile : ProfileType()
    data object OtherProfile : ProfileType()
}

sealed class ProfileEvent {
    object Refresh : ProfileEvent()
}