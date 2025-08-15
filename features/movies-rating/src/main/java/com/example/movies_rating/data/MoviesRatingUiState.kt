package com.example.movies_rating.data


import com.example.movies.api.model.RatingResponse


sealed class MoviesRatingUiState {
    object Idle : MoviesRatingUiState()
    object Loading : MoviesRatingUiState()
    data class MovieRated(val response: RatingResponse) : MoviesRatingUiState()
    data class Error(val throwable: Throwable) : MoviesRatingUiState()
}
