package com.example.jokesapp.data.repository

import android.content.Context
import android.util.Log
import com.example.jokesapp.common.State
import com.example.jokesapp.data.remote.JokesApi
import com.example.jokesapp.domain.model.Joke
import com.example.jokesapp.domain.repository.JokesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class JokesRepositoryImpl(
    private val context: Context,
    private val api: JokesApi
) : JokesRepository {

    override suspend fun getJokes(categoryId: Int): State<List<Joke>> {
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
            return State.success(jokes.toList())
        }
        return State.failed("Error while getting jokes")
    }

    companion object {
        private const val PAGE_SIZE = 10
        private const val ERROR_RETRY_COUNT = 3
    }
}