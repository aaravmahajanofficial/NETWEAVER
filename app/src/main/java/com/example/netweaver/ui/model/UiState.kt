package com.example.netweaver.ui.model

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: Throwable) : UiState<Nothing>()
}