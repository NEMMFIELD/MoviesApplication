package com.example.movies_details.domain

import com.example.movies.api.model.MovieActorsResponse
import com.example.movies.api.model.MovieDetailsResponse

interface MovieDetailsRepository {
    suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse
    suspend fun getMovieActors(movieId: Int): MovieActorsResponse
}
