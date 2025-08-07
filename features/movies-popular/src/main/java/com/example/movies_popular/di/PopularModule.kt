package com.example.movies_popular.di

import androidx.lifecycle.ViewModelProvider
import com.example.movies.api.MoviesApi
import com.example.movies_popular.data.PopularRepositoryImpl
import com.example.movies_popular.domain.GetPopularMoviesUseCase
import com.example.movies_popular.domain.PopularRepository
import com.example.movies_popular.ui.PopularViewModelFactory
import dagger.Module
import dagger.Provides

@Module(includes = [PopularViewModelModule::class])
class PopularModule {
    @Provides
    fun providePopularViewModelFactory(useCase: GetPopularMoviesUseCase): ViewModelProvider.Factory {
        return PopularViewModelFactory(useCase)
    }

    @Provides
    fun provideNoPopularRepository(api: MoviesApi): PopularRepository {
        return PopularRepositoryImpl(api)
    }
}
