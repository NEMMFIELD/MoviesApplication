package com.example.movies_upcoming.data

import com.example.movies.api.MoviesApi
import com.example.movies.api.model.DTOResponse
import com.example.movies_upcoming.domain.UpcomingRepository
import javax.inject.Inject

class UpcomingRepositoryImpl @Inject constructor(private val moviesApi: MoviesApi) :
    UpcomingRepository {
    override suspend fun getUpcomingMovies(page: Int): DTOResponse {
        return moviesApi.getUpcomingMovies(page = page)
    }
}
