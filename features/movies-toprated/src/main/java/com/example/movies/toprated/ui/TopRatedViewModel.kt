package com.example.movies.toprated.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core_model.MovieModel
import com.example.movies.toprated.domain.GetTopRatedMoviesUseCase
import com.example.core_ui.State
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopRatedViewModel @Inject constructor(private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase) :
    ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _topRatedMoviesValue.update { State.Failure(throwable) }
        _isLoading.value = false
    }

    private val _topRatedMoviesValue: MutableStateFlow<State<List<MovieModel>?>> =
        MutableStateFlow(State.Empty)

    val topRatedMoviesValue: StateFlow<State<List<MovieModel>?>>
        get() = _topRatedMoviesValue

    private var lastSavedPosition: Pair<Int, Int>? = null

    var currentPage = 1
        private set

    var isLastPage = false
        private set

    var isFirstLoad = true
        private set

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val loadedPopularMovies = mutableListOf<MovieModel>()

    init {
        loadTopRatedMovies()
    }

    fun saveScrollPosition(index: Int, offset: Int) {
        // Игнорируем позицию (0,0), если уже была сохранена
        //if (index == 0 && offset == 0 && lastSavedPosition != null) return

        // Игнорируем резкие скачки (например, при ресете списка)
        lastSavedPosition?.let { (lastIndex, _) ->
            if (kotlin.math.abs(index - lastIndex) > 20) return
        }

        lastSavedPosition = index to offset
    }

    fun getScrollPosition(): Pair<Int, Int> {
        return lastSavedPosition ?: (0 to 0)
    }

    fun loadTopRatedMovies() {
        if (_isLoading.value || isLastPage) return

        _isLoading.value = true

        viewModelScope.launch(coroutineExceptionHandler) {
            val newNowPlayingMovies = getTopRatedMoviesUseCase.execute(currentPage)
            newNowPlayingMovies.collect { movies ->
                if (movies.isNullOrEmpty()) {
                    isLastPage = true
                } else {
                    loadedPopularMovies.addAll(movies)
                    _topRatedMoviesValue.value = State.Success(loadedPopularMovies.toList())
                    currentPage++
                }
            }
            _isLoading.value = false

        }
    }
}

class TopRatedViewModelFactory @Inject constructor(
    private val getPopularMoviesUseCase: GetTopRatedMoviesUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopRatedViewModel::class.java)) {
            return TopRatedViewModel(getPopularMoviesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
