package com.example.jokesapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokesapp.common.LoadState
import com.example.jokesapp.common.State
import com.example.jokesapp.domain.model.Joke
import com.example.jokesapp.domain.repository.JokesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CategoryDetailsViewModel @Inject constructor(private val repository: JokesRepository) :
    ViewModel() {

    private val currentJokes = ArrayList<Joke>()

    private val _jokes = MutableStateFlow(ArrayList<Joke>())
    val jokes = _jokes.asStateFlow()

    // get cached data only in first query
    var isFirstQuery = true

    fun getJokes(categoryId: Int): SharedFlow<LoadState> {
        val jokesFlow: SharedFlow<LoadState> = flow {
            emit(LoadState.Loading)

            val result = repository.getJokes(categoryId, searchInCache = isFirstQuery)

            when(result) {
                is State.Success -> {
                    emit(LoadState.Success)
                    currentJokes.addAll(result.data)
                    val newArray = ArrayList<Joke>()
                    newArray.addAll(currentJokes)
                    _jokes.value = newArray
                    isFirstQuery = false
                }

                is State.Failed -> {
                    emit(LoadState.Failed)
                }

                is State.Loading -> {
                    emit(LoadState.Loading)
                }
            }

        }.shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 0
        )

        return jokesFlow
    }

    fun getJokesFromCache(categoryId: Int): SharedFlow<LoadState> {
        val jokesFlow: SharedFlow<LoadState> = flow {
            emit(LoadState.Loading)

            val result = repository.getJokesFromCache(categoryId)

            when(result) {
                is State.Success -> {
                    emit(LoadState.Success)
                    currentJokes.addAll(result.data)
                    val newArray = ArrayList<Joke>()
                    newArray.addAll(currentJokes)
                    _jokes.value = newArray
                    isFirstQuery = false
                }

                is State.Failed -> {
                    emit(LoadState.Failed)
                }

                is State.Loading -> {
                    emit(LoadState.Loading)
                }
            }

        }.shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 0
        )

        return jokesFlow
    }
}