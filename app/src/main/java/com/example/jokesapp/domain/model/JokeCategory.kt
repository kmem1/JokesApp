package com.example.jokesapp.domain.model

class JokeCategory(val id: Int, val name: String) {

    companion object {
        fun getAllCategories(): ArrayList<JokeCategory> {
            return arrayListOf(
                JokeCategory(1, "Анекдоты" ),
                JokeCategory(11, "Анекдоты(+18)" ),
                JokeCategory(2, "Рассказы" ),
                JokeCategory(12, "Рассказы(+18)" ),
                JokeCategory(3, "Стишки" ),
                JokeCategory(13, "Стишки(+18)" ),
                JokeCategory(4, "Афоризмы" ),
                JokeCategory(14, "Афоризмы(+18)" ),
                JokeCategory(5, "Цитаты" ),
                JokeCategory(15, "Цитаты(+18)" ),
                JokeCategory(6, "Тосты" ),
                JokeCategory(16, "Тосты(+18)" ),
                JokeCategory(8, "Статусы" ),
                JokeCategory(18, "Статусы(+18)" ),
            )
        }

    }
}