package com.example.jokesapp.domain.repository

import com.example.jokesapp.domain.model.Joke

interface JokesRepository {

    suspend fun getJokes(categoryId: Int): List<Joke>
}