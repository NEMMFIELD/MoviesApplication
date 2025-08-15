package com.example.movies_rating.domain

import com.example.movies.api.model.RatingResponse
import javax.inject.Inject

class RateMoviesUseCase @Inject constructor(
    private val moviesRatingRepository: MoviesRatingRepository
) {
    suspend fun execute(movieId: Int, rating: Double,sessionId:String): RatingResponse {
        return moviesRatingRepository.rateMovie(sessionId, movieId, rating)
    }
}
