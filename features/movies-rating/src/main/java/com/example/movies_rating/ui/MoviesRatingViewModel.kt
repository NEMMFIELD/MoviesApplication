package com.example.movies_rating.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movies_rating.data.MoviesRatingUiState
import com.example.movies_rating.domain.RateMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesRatingViewModel @Inject constructor(
    private val rateMoviesUseCase: RateMoviesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<MoviesRatingUiState>(MoviesRatingUiState.Idle)
    val uiState: StateFlow<MoviesRatingUiState> = _uiState



    fun rateMovie(movieId: Int, rating: Double,sessionId:String) {
        viewModelScope.launch {
            _uiState.value = MoviesRatingUiState.Loading
            try {
                val ratingResponse = rateMoviesUseCase.execute(movieId, rating, sessionId)
                _uiState.value = MoviesRatingUiState.MovieRated(ratingResponse)
            } catch (e: Exception) {
                _uiState.value = MoviesRatingUiState.Error(e)
            }
        }
    }
}

class MoviesRatingViewModelFactory @Inject constructor(
    private val rateMoviesUseCase: RateMoviesUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesRatingViewModel::class.java)) {
            return MoviesRatingViewModel(
                rateMoviesUseCase,
                ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
