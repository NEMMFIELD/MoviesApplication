package com.example.movies_rating.domain

import com.example.movies.api.model.CreateSessionResponse
import com.example.movies.api.model.RatingResponse
import com.example.movies.api.model.RequestTokenResponse

interface MoviesRatingRepository {
    suspend fun rateMovie(sessionId: String, movieId: Int, rating: Double): RatingResponse
}
