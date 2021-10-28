package com.example.jokesapp.domain.model

import androidx.room.Entity

@Entity(tableName = "jokes")
data class Joke(
    val content: String
)