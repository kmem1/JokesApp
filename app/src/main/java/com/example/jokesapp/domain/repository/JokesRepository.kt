package com.example.jokesapp.domain.repository

import com.example.jokesapp.common.State
import com.example.jokesapp.domain.model.Joke

interface JokesRepository {

    suspend fun getJokes(categoryId: Int, searchInCache: Boolean = false): State<List<Joke>>
    suspend fun getJokesFromCache(categoryId: Int): State<List<Joke>>
}