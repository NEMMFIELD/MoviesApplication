package com.example.movies_rating.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movies_rating.data.MoviesRatingUiState
import com.example.movies_rating.domain.GetRatingStatusUseCase
import com.example.movies_rating.domain.GetSessionIdUseCase
import com.example.movies_rating.domain.RateMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesRatingViewModel @Inject constructor(
    private val rateMoviesUseCase: RateMoviesUseCase,
    private val getSessionIdUseCase: GetSessionIdUseCase,
    private val getRatingStatusUseCase: GetRatingStatusUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<MoviesRatingUiState>(MoviesRatingUiState.Idle)
    val uiState: StateFlow<MoviesRatingUiState> = _uiState


    fun rateMovie(movieId: Int, rating: Double) {
        viewModelScope.launch {
            _uiState.value = MoviesRatingUiState.Loading
            try {
                val sessionId = getSessionIdUseCase.execute()
                if (sessionId == null) {
                    _uiState.value = MoviesRatingUiState.Error(Throwable("No session ID"))
                    return@launch
                }
                val ratingResponse = rateMoviesUseCase.execute(movieId, rating, sessionId)
                _uiState.value = MoviesRatingUiState.MovieRated(ratingResponse)
            } catch (e: Exception) {
                _uiState.value = MoviesRatingUiState.Error(e)
            }
        }
    }
    fun loadRatingStatus(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = MoviesRatingUiState.Loading
            try {
                val sessionId = getSessionIdUseCase.execute()
                if (sessionId == null) {
                    _uiState.value = MoviesRatingUiState.Error(Throwable("No session ID"))
                    return@launch
                }
                val status = getRatingStatusUseCase.execute(sessionId, movieId)
                _uiState.value = MoviesRatingUiState.RatingStatusLoaded(status)
            } catch (e: Exception) {
                _uiState.value = MoviesRatingUiState.Error(e)
                Log.d("Rating Error",e.toString())
            }
        }
    }
}



class MoviesRatingViewModelFactory @Inject constructor(
    private val rateMoviesUseCase: RateMoviesUseCase,
    private val getSessionIdUseCase: GetSessionIdUseCase,
    private val getRatingStatusUseCase: GetRatingStatusUseCase,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesRatingViewModel::class.java)) {
            return MoviesRatingViewModel(
                rateMoviesUseCase, getSessionIdUseCase, getRatingStatusUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
