package com.example.netweaver.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netweaver.domain.model.Post
import com.example.netweaver.domain.model.User
import com.example.netweaver.domain.usecase.posts.GetPostsUseCase
import com.example.netweaver.domain.usecase.posts.LikePostUseCase
import com.example.netweaver.domain.usecase.posts.UnLikePostUseCase
import com.example.netweaver.ui.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock.System.now
import javax.inject.Inject

val sampleUser = User(
    userId = "u1",
    email = "john.doe@example.com",
    firstName = "John",
    lastName = "Doe",
    fullName = "John Doe",
    headline = "Software Engineer",
    location = "New York, USA",
    about = "Passionate about technology and coding.",
    profileImageUrl = "https://example.com/john_doe.jpg"
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unLikePostUseCase: UnLikePostUseCase
) :
    ViewModel() {

    private val _homeUiState = MutableStateFlow(
        HomeState(
            posts = listOf(
                Post(
                    docId = "p1",
                    id = "1",
                    user = sampleUser,
                    content = "Exploring Kotlin and Jetpack Compose!",
                    mediaUrl = listOf("https://example.com/image1.jpg"),
                    isLiked = true,
                    likesCount = 12,
                    commentsCount = 3,
                    createdAt = now(),
                    updatedAt = now()
                ),
                Post(
                    docId = "p2",
                    id = "2",
                    user = sampleUser,
                    content = "Just finished a great project with the team.",
                    mediaUrl = listOf("https://example.com/image2.jpg"),
                    isLiked = false,
                    likesCount = 30,
                    commentsCount = 5,
                    createdAt = now(),
                    updatedAt = now()
                ),
                Post(
                    docId = "p3",
                    id = "3",
                    user = sampleUser,
                    content = "Learning new things every day!",
                    mediaUrl = listOf(),
                    isLiked = false,
                    likesCount = 8,
                    commentsCount = 1,
                    createdAt = now(),
                    updatedAt = now()
                )
            )
        )
    )
    val homeUiState: StateFlow<HomeState> = _homeUiState.asStateFlow()

    init {
//        onEvent(HomeEvent.GetPosts)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.GetPosts -> {

                if (_homeUiState.value.posts == null) {
                    getPosts()
                }

            }

            is HomeEvent.LikePost -> {

                viewModelScope.launch {

                    if (event.post.id in _homeUiState.value.reactionQueue) {
                        return@launch
                    }

                    _homeUiState.update { it.copy(reactionQueue = _homeUiState.value.reactionQueue + event.post.id) }

                    when (val result = likePostUseCase(post = event.post)) {
                        is Result.Success -> {
                            _homeUiState.update { it.copy(success = "Successfully liked the post") }
                        }

                        is Result.Error -> {
                            _homeUiState.update { it.copy(error = result.exception.message) }
                        }
                    }

                    _homeUiState.update { it.copy(reactionQueue = _homeUiState.value.reactionQueue - event.post.id) }
                }

            }

            is HomeEvent.UnLikePost -> {

                viewModelScope.launch {

                    if (event.post.id in _homeUiState.value.reactionQueue) {
                        return@launch
                    }

                    _homeUiState.update { it.copy(reactionQueue = _homeUiState.value.reactionQueue + event.post.id) }

                    when (val result = unLikePostUseCase(post = event.post)) {
                        is Result.Success -> {
                            _homeUiState.update { it.copy(success = "Successfully unliked the post") }
                        }

                        is Result.Error -> {
                            _homeUiState.update { it.copy(error = result.exception.message) }
                        }
                    }

                    _homeUiState.update { it.copy(reactionQueue = _homeUiState.value.reactionQueue - event.post.id) }

                }

            }
        }
    }

    private fun getPosts() {
        viewModelScope.launch {

            _homeUiState.update { it.copy(isLoading = true, error = null) }

            try {
                getPostsUseCase().flowOn(Dispatchers.IO)
                    .collect { result ->
                        when (result) {
                            is Result.Success -> {
                                _homeUiState.update {
                                    it.copy(
                                        isLoading = false,
                                        posts = result.data
                                    )
                                }
                            }

                            is Result.Error -> {
                                _homeUiState.update {
                                    it.copy(
                                        isLoading = false,
                                        error = result.exception.message
                                    )
                                }
                            }
                        }
                    }
            } catch (e: Exception) {
                _homeUiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }

}

data class HomeState(
    val isLoading: Boolean = false,
    val posts: List<Post>? = null,
    val reactionQueue: Set<String> = emptySet(),
    val error: String? = null,
    val success: String? = null
)


sealed class HomeEvent {

    object GetPosts : HomeEvent()
    data class LikePost(val post: Post) : HomeEvent()
    data class UnLikePost(val post: Post) : HomeEvent()

}