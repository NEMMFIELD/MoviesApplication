package com.example.movies_details.ui

import android.util.Log
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.example.movies_details.data.MovieActorsModel
import com.example.movies_details.data.MovieDetailsModel
import com.example.movies_details.domain.GetMovieActorsUseCase
import com.example.movies_details.domain.GetMovieDetailsUseCase
import com.example.core_ui.State
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsViewModel @AssistedInject constructor(
    private val movieDetailsUseCase: GetMovieDetailsUseCase,
    private val movieActorsUseCase: GetMovieActorsUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): MovieDetailsViewModel
    }


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _movieDetailsValue.update { State.Failure(throwable) }
    }

    private val _movieDetailsValue: MutableStateFlow<State<MovieDetailsModel>?> =
        MutableStateFlow(State.Empty)

    val movieDetailsValue: StateFlow<State<MovieDetailsModel>?>
        get() = _movieDetailsValue

    private val _movieDetailsActors: MutableStateFlow<State<List<MovieActorsModel>?>> =
        MutableStateFlow(State.Empty)

    val movieDetailsActors: StateFlow<State<List<MovieActorsModel>?>>
        get() = _movieDetailsActors


    fun loadMovieDetails(movieId: Int?) {
        viewModelScope.launch(coroutineExceptionHandler) {
           // Log.d("MovieID", movieId.toString())
            movieDetailsUseCase.execute(movieId = movieId ?: 0).collect { movieDetailsInfo ->
                _movieDetailsValue.update { State.Success(movieDetailsInfo) }
            }
        }
    }

    fun loadMovieActors(movieId: Int?) {
        viewModelScope.launch(coroutineExceptionHandler) {
            movieActorsUseCase.execute(movieId = movieId ?: 0).collect { actors ->
                _movieDetailsActors.update { State.Success(actors) }

            }
        }
    }
}

class MovieDetailsViewModelFactoryImpl @Inject constructor(
    private val assistedFactory: MovieDetailsViewModel.Factory
) {
    fun provideFactory(owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return object : AbstractSavedStateViewModelFactory(owner, null) {
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                @Suppress("UNCHECKED_CAST")
                return assistedFactory.create(handle) as T
            }
        }
    }
}
