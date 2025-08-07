package com.example.movies.toprated.di

import androidx.lifecycle.ViewModelProvider
import com.example.movies.api.MoviesApi
import com.example.movies.toprated.data.TopRatedRepositoryImpl
import com.example.movies.toprated.domain.GetTopRatedMoviesUseCase
import com.example.movies.toprated.domain.TopRatedRepository
import com.example.movies.toprated.ui.TopRatedViewModelFactory
import dagger.Module
import dagger.Provides

@Module(includes = [TopRatedViewModelModule::class])
class TopRatedModule {
    @Provides
    fun providePopularViewModelFactory(useCase: GetTopRatedMoviesUseCase): ViewModelProvider.Factory {
        return TopRatedViewModelFactory(useCase)
    }

    @Provides
    fun provideNoPopularRepository(api: MoviesApi): TopRatedRepository {
        return TopRatedRepositoryImpl(api)
    }

}
