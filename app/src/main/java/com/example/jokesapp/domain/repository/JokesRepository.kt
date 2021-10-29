package com.example.jokesapp.domain.repository

import com.example.jokesapp.common.State
import com.example.jokesapp.domain.model.Joke
import kotlinx.coroutines.flow.Flow

interface JokesRepository {

    suspend fun getJokes(categoryId: Int): State<List<Joke>>
}