package com.example.movies_details.di

import com.example.movies.api.MoviesApi
import com.example.movies_details.data.MovieDetailsRepositoryImpl
import com.example.movies_details.domain.MovieDetailsRepository
import dagger.Module
import dagger.Provides

@Module
class MovieDetailsModule {
    @Provides
    fun provideMovieDetailsRepository(api: MoviesApi): MovieDetailsRepository {
        return MovieDetailsRepositoryImpl(api)
    }
}
