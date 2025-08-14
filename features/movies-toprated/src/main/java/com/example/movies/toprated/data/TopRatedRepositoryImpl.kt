package com.example.movies.toprated.data

import com.example.movies.api.MoviesApi
import com.example.movies.api.model.DTOResponse
import com.example.movies.toprated.domain.TopRatedRepository
import javax.inject.Inject

class TopRatedRepositoryImpl @Inject constructor(private val moviesApi: MoviesApi) :
    TopRatedRepository {
    override suspend fun loadTopRatedMovies(page: Int): DTOResponse {
        return moviesApi.getTopRatedMovies(page = page)
    }
}
