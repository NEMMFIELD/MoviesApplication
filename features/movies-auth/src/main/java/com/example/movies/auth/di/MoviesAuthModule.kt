package com.example.movies.auth.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import com.example.movies.api.MoviesApi
import com.example.movies.auth.data.MoviesAuthRepositoryImpl
import com.example.movies.auth.domain.CreateRequestTokenUseCase
import com.example.movies.auth.domain.CreateSessionUseCase
import com.example.movies.auth.domain.MoviesAuthRepository
import com.example.movies.auth.domain.SaveSessionIdUseCase
import com.example.movies.auth.ui.MoviesAuthViewModelFactory
import dagger.Module
import dagger.Provides

@Module(includes = [MoviesAuthViewModelModule::class])
class MoviesAuthModule {

    @Provides
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences("movies_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideMoviesRatingRepository(
        api: MoviesApi, prefs: SharedPreferences
    ): MoviesAuthRepository {
        return MoviesAuthRepositoryImpl(api, prefs)
    }

    @Provides
    fun provideMoviesAuthViewModelFactory(
        createRequestTokenUseCase: CreateRequestTokenUseCase,
        createSession: CreateSessionUseCase,
        saveSessionId: SaveSessionIdUseCase
    ): ViewModelProvider.Factory {
        return MoviesAuthViewModelFactory(createRequestTokenUseCase, createSession, saveSessionId)
    }
}
