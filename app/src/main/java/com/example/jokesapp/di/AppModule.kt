package com.example.jokesapp.di

import android.content.Context
import com.example.jokesapp.data.remote.JokesApi
import com.example.jokesapp.data.repository.JokesRepositoryImpl
import com.example.jokesapp.domain.model.Joke
import com.example.jokesapp.domain.repository.JokesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideJokesApi(): JokesApi {
        return Retrofit.Builder()
            .baseUrl("http://rzhunemogu.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JokesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideJokesRepository(
        @ApplicationContext context: Context,
        api: JokesApi
    ): JokesRepository {
        return JokesRepositoryImpl(context, api)
    }
}