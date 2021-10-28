package com.example.jokesapp.data.repository

import android.content.Context
import com.example.jokesapp.data.remote.JokesApi
import com.example.jokesapp.domain.model.Joke
import com.example.jokesapp.domain.repository.JokesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JokesRepositoryImpl(
    private val context: Context,
    private val api: JokesApi
) : JokesRepository {

    override suspend fun getJokes(categoryId: Int): List<Joke> {
        val result = ArrayList<Joke>()

        withContext(Dispatchers.IO) {
            for (i in 0..10) {
                result.add(api.getJoke(categoryId))
            }
        }

        return result
    }
}