package com.example.netweaver.ui.features.createPost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netweaver.domain.usecase.posts.CreatePostUseCase
import com.example.netweaver.ui.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(private val createPostUseCase: CreatePostUseCase) :
    ViewModel() {

    private val _createPostUiState = MutableStateFlow<CreatePostState>(CreatePostState.Idle)
    val createPostState = _createPostUiState.asStateFlow()

    fun createPost(
        content: String,
        byteArrayList: List<ByteArray?>?,
        fileExtensions: List<String?>
    ) {

        viewModelScope.launch {

            _createPostUiState.value = CreatePostState.Loading

            when (val result = createPostUseCase(content, byteArrayList, fileExtensions)) {
                is Result.Success -> {
                    _createPostUiState.value = CreatePostState.Success
                }

                is Result.Error -> {
                    _createPostUiState.value = CreatePostState.Error(
                        message = result.exception.message ?: "Unknown error occurred"
                    )
                }
            }
        }


    }
}

sealed interface CreatePostState {
    data object Idle : CreatePostState
    data object Loading : CreatePostState
    data object Success : CreatePostState
    data class Error(
        val message: String
    ) : CreatePostState
}