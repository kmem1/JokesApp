package com.example.jokesapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokesapp.common.State
import com.example.jokesapp.domain.model.Joke
import com.example.jokesapp.domain.repository.JokesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CategoryDetailsViewModel @Inject constructor(private val repository: JokesRepository) :
    ViewModel() {

    fun getJokes(categoryId: Int): StateFlow<State<List<Joke>>> {
        val jokesFlow: StateFlow<State<List<Joke>>> = flow {
            emit(repository.getJokes(categoryId))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = State.loading()
        )

        return jokesFlow
    }
}