package com.example.jokesapp.domain.model

class JokeCategory {

    companion object {
        fun getAllCategories(): ArrayList<String> {
            return arrayListOf(
                "Анекдоты",
                "Анекдоты(+18)",
                "Рассказы",
                "Рассказы(+18)",
                "Стишки",
                "Стишки(+10)",
                "Афоризмы",
                "Афоризмы(+10)",
                "Цитаты",
                "Цитаты(+10)",
                "Тосты",
                "Тосты(+10)",
                "Статусы",
                "Статусы(+10)"
            )
        }

    }
}