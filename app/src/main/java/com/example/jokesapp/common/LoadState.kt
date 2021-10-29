package com.example.jokesapp.common

sealed class LoadState {
    object Loading : LoadState()
    object Success : LoadState()
    object Failed : LoadState()
}