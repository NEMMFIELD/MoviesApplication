package com.example.movies_popular.data

import com.example.movies.api.MoviesApi
import com.example.movies.api.model.DTOResponse
import com.example.movies_popular.domain.PopularRepository
import jakarta.inject.Inject

class PopularRepositoryImpl @Inject constructor(val moviesApi: MoviesApi): PopularRepository {
    override suspend fun loadPopularMovies(page: Int): DTOResponse {
        return moviesApi.getPopularMovies(apiKey = "56b9fc3e2f7cf0c570b8d7dc71de180e",page =page)
    }
}
