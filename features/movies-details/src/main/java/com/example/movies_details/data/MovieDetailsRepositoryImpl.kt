package com.example.movies_details.data

import com.example.movies.api.MoviesApi
import com.example.movies.api.model.MovieActorsResponse
import com.example.movies.api.model.MovieDetailsResponse
import com.example.movies_details.domain.MovieDetailsRepository
import javax.inject.Inject

class MovieDetailsRepositoryImpl @Inject constructor(private val moviesApi: MoviesApi) :
    MovieDetailsRepository {
    override suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse {
        return moviesApi.getMovieDetails(
            apiKey = "56b9fc3e2f7cf0c570b8d7dc71de180e",
            movieId = movieId
        )
    }

    override suspend fun getMovieActors(movieId: Int): MovieActorsResponse {
        return moviesApi.getMovieActors(apiKey = "56b9fc3e2f7cf0c570b8d7dc71de180e",
                                        movieId = movieId)
    }
}
