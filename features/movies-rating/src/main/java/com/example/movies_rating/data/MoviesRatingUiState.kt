package com.example.movies_rating.data


import com.example.movies.api.model.RatingResponse
import com.example.movies.api.model.RatingStatus


sealed class MoviesRatingUiState {
    object Idle : MoviesRatingUiState()
    object Loading : MoviesRatingUiState()
    data class MovieRated(val response: RatingResponse) : MoviesRatingUiState()
    data class RatingStatusLoaded(val status: Boolean) : MoviesRatingUiState()
    data class Error(val throwable: Throwable) : MoviesRatingUiState()
}
