package com.example.movies.nowplaying.ui


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core_model.MovieModel
import com.example.movies.nowplaying.domain.NowPlayingMoviesUseCase
import com.example.state.State
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class NowPlayingViewModel @Inject constructor(
    private val nowPlayingMoviesUseCase: NowPlayingMoviesUseCase
) : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _nowPlayingMoviesValue.update { State.Failure(throwable) }
        _isLoading.value = false
    }

    private var lastSavedPosition: Pair<Int, Int>? = null

    private val _nowPlayingMoviesValue = MutableStateFlow<State<List<MovieModel>?>>(State.Empty)
    val nowPlayingMoviesValue: StateFlow<State<List<MovieModel>?>> get() = _nowPlayingMoviesValue

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val loadedNowPlayingMovies = mutableListOf<MovieModel>()

    var currentPage = 1
        private set

    var isLastPage = false
        private set

    var isFirstLoad = true
        private set

    init {
        Log.d("ViewModelCheck", "NowPlayingViewModel created: $this")
        loadNowPlayingMovies()
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

    fun loadNowPlayingMovies() {
        if (_isLoading.value || isLastPage) return

        _isLoading.value = true

        viewModelScope.launch(coroutineExceptionHandler) {
            val movies = nowPlayingMoviesUseCase.execute(currentPage).first() // вместо collect
            if (movies.isNullOrEmpty()) {
                isLastPage = true
            } else {
                loadedNowPlayingMovies.addAll(movies)
                _nowPlayingMoviesValue.value = State.Success(loadedNowPlayingMovies.toList())

                // Первая загрузка прошла — выключаем флаг
                if (isFirstLoad) isFirstLoad = false

                currentPage++
            }
            _isLoading.value = false
        }
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
