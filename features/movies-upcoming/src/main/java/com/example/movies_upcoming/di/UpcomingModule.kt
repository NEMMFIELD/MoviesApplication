package com.example.movies_upcoming.di

import androidx.lifecycle.ViewModelProvider
import com.example.movies.api.MoviesApi
import com.example.movies_upcoming.data.UpcomingRepositoryImpl
import com.example.movies_upcoming.domain.GetUpcomingMoviesUseCase
import com.example.movies_upcoming.domain.UpcomingRepository
import com.example.movies_upcoming.ui.UpcomingViewModelFactory
import dagger.Module
import dagger.Provides

@Module(includes = [UpcomingViewModelModule::class])
class UpcomingModule {
    @Provides
    fun provideUpcomingViewModelFactory(useCase: GetUpcomingMoviesUseCase): ViewModelProvider.Factory {
        return UpcomingViewModelFactory(useCase)
    }

    @Provides
    fun provideUpcomingRepository(api: MoviesApi): UpcomingRepository {
        return UpcomingRepositoryImpl(api)
    }
}
