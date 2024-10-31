package com.example.netweaver.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netweaver.domain.model.Post
import com.example.netweaver.domain.usecase.GetPostsUseCase
import com.example.netweaver.ui.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val getPostsUseCase: GetPostsUseCase) :
    ViewModel() {

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState = _homeUiState.asStateFlow()

    init {
        getPosts()
    }

    fun getPosts() {

        viewModelScope.launch {

            _homeUiState.value = HomeUiState.Loading

            getPostsUseCase().flowOn(Dispatchers.IO).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _homeUiState.value = HomeUiState.Success(result.data)
                    }

                    is Result.Error -> {
                        _homeUiState.value = HomeUiState.Error(result.exception)
                    }
                }
            }


        }
    }


}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val data: List<Post>) : HomeUiState
    data class Error(val exception: Throwable) : HomeUiState
}