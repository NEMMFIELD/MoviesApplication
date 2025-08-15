package com.example.movies_rating.data

import com.example.movies.api.MoviesApi
import com.example.movies.api.model.RatingRequest
import com.example.movies.api.model.RatingResponse
import com.example.movies_rating.domain.MoviesRatingRepository
import javax.inject.Inject

class MoviesRatingRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi,
) :
    MoviesRatingRepository {
    override suspend fun rateMovie(
        sessionId: String,
        movieId: Int,
        rating: Double
    ): RatingResponse {
        return moviesApi.addRating(movieId = movieId, sessionId, RatingRequest(rating))
    }
}
