package com.example.movies_rating.data

import android.content.SharedPreferences
import com.example.movies.api.MoviesApi
import com.example.movies.api.model.RatingRequest
import com.example.movies.api.model.RatingResponse
import com.example.movies_rating.domain.MoviesRatingRepository
import javax.inject.Inject

class MoviesRatingRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi,
    private val sharedPreferences: SharedPreferences
) :
    MoviesRatingRepository {
    override suspend fun rateMovie(
        sessionId: String,
        movieId: Int,
        rating: Double
    ): RatingResponse {
        return moviesApi.addRating(movieId = movieId, sessionId, RatingRequest(rating))
    }

    override fun getSessionId(): String? {
        return sharedPreferences.getString("session_id",null)
    }
}
