package com.example.movies.nowplaying.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movies.nowplaying.data.MovieModel
import com.example.movies.nowplaying.domain.NowPlayingMoviesUseCase
import com.example.state.State
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class NowPlayingViewModel @Inject constructor(private val nowPlayingMoviesUseCase: NowPlayingMoviesUseCase) :
    ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _nowPlayingMoviesValue.update { State.Failure(throwable) }
        _isLoading.value = false
    }

    private val _nowPlayingMoviesValue: MutableStateFlow<State<List<MovieModel>?>> =
        MutableStateFlow(State.Empty)

    val nowPlayingMoviesValue: StateFlow<State<List<MovieModel>?>>
        get() = _nowPlayingMoviesValue
            .onStart { loadNowPlayingMovies() }
            .stateIn(
                viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = State.Empty
            )
    private var currentPage = 1
     var isLastPage = false
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val loadedNowPlayingMovies = mutableListOf<MovieModel>()

     fun loadNowPlayingMovies() {
        if (_isLoading.value || isLastPage) return
        _isLoading.value = true

        viewModelScope.launch(coroutineExceptionHandler) {
            val newNowPlayingMovies = nowPlayingMoviesUseCase.execute(currentPage)
            newNowPlayingMovies.collect { movies ->
                if (movies.isNullOrEmpty()) {
                    isLastPage = true
                } else {
                    loadedNowPlayingMovies.addAll(movies)
                    _nowPlayingMoviesValue.value = State.Success(loadedNowPlayingMovies.toList())
                    currentPage++
                }
            }
            _isLoading.value = false

        }
    }

    override fun onCleared() {
        super.onCleared()
        loadedNowPlayingMovies.clear()
    }
}

class NowPlayingViewModelFactory @Inject constructor(
    private val useCase: NowPlayingMoviesUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NowPlayingViewModel::class.java)) {
            return NowPlayingViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
