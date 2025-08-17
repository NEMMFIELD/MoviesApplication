package com.example.movies_rating.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import com.example.movies.api.MoviesApi
import com.example.movies_rating.data.MoviesRatingRepositoryImpl
import com.example.movies_rating.domain.GetRatingStatusUseCase
import com.example.movies_rating.domain.GetSessionIdUseCase
import com.example.movies_rating.domain.MoviesRatingRepository
import com.example.movies_rating.domain.RateMoviesUseCase
import com.example.movies_rating.ui.MoviesRatingViewModelFactory
import dagger.Module
import dagger.Provides

@Module(includes = [MoviesRatingViewModelModule::class])
class MoviesRatingModule {

    @Provides
    fun provideMoviesratingViewModelFactory(
        rateMovieUseCase: RateMoviesUseCase,
        getSessionIdUseCase: GetSessionIdUseCase,
        getRatingStatusUseCase: GetRatingStatusUseCase,
    ): ViewModelProvider.Factory {
        return MoviesRatingViewModelFactory(
            rateMovieUseCase,
            getSessionIdUseCase = getSessionIdUseCase,
            getRatingStatusUseCase
        )
    }

    @Provides
    fun provideMoviesRatingRepository(
        api: MoviesApi, sharedPreferences: SharedPreferences
    ): MoviesRatingRepository {
        return MoviesRatingRepositoryImpl(api,sharedPreferences)
    }
}
