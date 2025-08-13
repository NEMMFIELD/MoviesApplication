package com.example.movies_popular.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core_model.MovieModel
import com.example.movies_popular.domain.GetPopularMoviesUseCase
import com.example.state.State
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularViewModel @Inject constructor(private val getPopularMoviesUseCase: GetPopularMoviesUseCase):
    ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _popularMoviesValue.update { com.example.state.State.Failure(throwable) }
        _isLoading.value = false
    }

    private var lastSavedPosition:Pair<Int,Int> ? = null


    private val _popularMoviesValue: MutableStateFlow<State<List<MovieModel>?>> =
        MutableStateFlow(State.Empty)

    val popularMoviesValue: StateFlow<State<List<MovieModel>?>>
        get() = _popularMoviesValue

    var currentPage = 1
        private set

    var isLastPage = false
        private set

    var isFirstLoad = false
        private set

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val loadedPopularMovies = mutableListOf<MovieModel>()

    init {
        loadPopularMovies()
    }

    fun saveScrollPosition(index:Int,offset:Int) {
        // Игнорируем резкие скачки (например, при ресете списка)
        lastSavedPosition?.let { (lastIndex, _) ->
            if (kotlin.math.abs(index - lastIndex) > 20) return
        }

        lastSavedPosition = index to offset
    }

    fun getScrollPosition(): Pair<Int, Int> {
        return lastSavedPosition ?: (0 to 0)
    }

    fun loadPopularMovies() {
        if (_isLoading.value || isLastPage) return

        _isLoading.value = true

        viewModelScope.launch(coroutineExceptionHandler) {
            val newNowPlayingMovies = getPopularMoviesUseCase.execute(currentPage)
            newNowPlayingMovies.collect { movies ->
                if (movies.isNullOrEmpty()) {
                    isLastPage = true
                } else {
                    loadedPopularMovies.addAll(movies)
                    _popularMoviesValue.value = State.Success(loadedPopularMovies.toList())
                    currentPage++
                }
            }
            _isLoading.value = false

        }
    }
}

class PopularViewModelFactory @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PopularViewModel::class.java)) {
            return PopularViewModel(getPopularMoviesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
