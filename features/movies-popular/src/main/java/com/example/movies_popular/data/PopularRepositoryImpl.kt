package com.example.movies_popular.data

import com.example.movies.api.MoviesApi
import com.example.movies.api.model.DTOResponse
import com.example.movies_popular.domain.PopularRepository
import jakarta.inject.Inject

class PopularRepositoryImpl @Inject constructor(val moviesApi: MoviesApi): PopularRepository {
    override suspend fun loadPopularMovies(page: Int): DTOResponse {
        return moviesApi.getPopularMovies(page =page)
    }
}
