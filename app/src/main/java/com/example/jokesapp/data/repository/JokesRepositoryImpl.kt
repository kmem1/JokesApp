package com.example.jokesapp.data.repository

import android.content.Context
import android.util.Log
import com.example.jokesapp.JokesApplication
import com.example.jokesapp.common.State
import com.example.jokesapp.data.local.AppDatabase
import com.example.jokesapp.data.remote.JokesApi
import com.example.jokesapp.domain.model.Joke
import com.example.jokesapp.domain.repository.JokesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class JokesRepositoryImpl(
    private val context: Context,
    private val api: JokesApi
) : JokesRepository {

    override suspend fun getJokes(categoryId: Int, searchInCache: Boolean): State<List<Joke>> {
        val jokes = ArrayList<Joke>()
        var errorCounter = 0

        outer@ while(errorCounter != ERROR_RETRY_COUNT) {
            for (i in 1..PAGE_SIZE) {

                val response = try {
                    api.getJoke(categoryId)
                } catch (e: IOException) {
                    Log.e("ERROR", "IOException, check the internet connection")
                    errorCounter += 1
                    delay(300)
                    continue@outer
                } catch (e: HttpException) {
                    Log.e("ERROR", "HttpException, unexpected response")
                    errorCounter += 1
                    delay(300)
                    continue@outer
                }

                if (response.isSuccessful && response.body() != null) {
                    jokes.add(response.body()!!)
                } else {
                    Log.e("ERROR", "Response not successful")
                    errorCounter += 1
                    delay(300)
                    continue@outer
                }
            }

            saveToCache(jokes)
            return State.success(jokes.toList())
        }

        return if (searchInCache) {
            getJokesFromCache(categoryId)
        } else {
            State.failed("Error while getting jokes")
        }
    }

    override suspend fun getJokesFromCache(categoryId: Int): State<List<Joke>> {
        if (categoryId != JokesApplication.getCachedCategoryIdFromPreferences()) {
            return State.failed("Error while getting jokes")
        }

        val result = ArrayList<Joke>()

        withContext(Dispatchers.IO) {
            result.addAll(AppDatabase.getInstance(context).jokeDao().getJokes())
        }

        return if (result.isNotEmpty()) {
            State.success(result)
        } else {
            State.failed("Error while getting jokes")
        }
    }


    private suspend fun saveToCache(items: ArrayList<Joke>) {
        withContext(Dispatchers.IO) {
            AppDatabase.getInstance(context).jokeDao().deleteAllJokes()
            AppDatabase.getInstance(context).jokeDao().insertJokes(items)
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
        private const val ERROR_RETRY_COUNT = 3
    }
}