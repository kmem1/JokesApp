package com.example.jokesapp.data.remote

import com.example.jokesapp.domain.model.Joke
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JokesApi {

    @GET("/RandJSON.aspx")
    suspend fun getJoke(@Query("CType") type: Int): Response<Joke>
}