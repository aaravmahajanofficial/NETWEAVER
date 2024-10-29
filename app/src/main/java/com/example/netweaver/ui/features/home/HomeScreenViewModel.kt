package com.example.netweaver.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netweaver.domain.model.Post
import com.example.netweaver.domain.usecase.GetPostsUseCase
import com.example.netweaver.ui.model.Result
import com.example.netweaver.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val getPostsUseCase: GetPostsUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Post>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getPosts()
    }

    fun getPosts() {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                _uiState.value = UiState.Loading

                when (val result = getPostsUseCase()) {

                    is Result.Success -> {
                        _uiState.value = UiState.Success(
                            data = result.data
                        )
                    }

                    is Result.Error -> {
                        _uiState.value = UiState.Error(result.exception)
                    }
                }
            }
        }
    }


}