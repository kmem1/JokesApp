package com.example.jokesapp.data.local

import androidx.room.*
import com.example.jokesapp.domain.model.Joke

@Dao
interface JokeDao {

    @Query("SELECT * FROM jokes")
    fun getJokes(): List<Joke>

    @Query("DELETE FROM jokes")
    fun deleteAllJokes()

    @Insert
    fun insertJokes(jokes: List<Joke>)

    @Delete
    fun deleteJokes(jokes: List<Joke>)
}