package com.example.movies.actorfilms.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movies.actorfilms.data.ActorMovieCreditsModel
import com.example.movies.actorfilms.domain.GetActorMovieCreditsUseCase
import com.example.state.State
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActorMovieCreditsViewModel @Inject constructor(private val getActorMovieCreditsUseCase: GetActorMovieCreditsUseCase) :
    ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _actorMovieCreditsValue.update { State.Failure(throwable) }
    }

    private val _actorMovieCreditsValue: MutableStateFlow<State<List<ActorMovieCreditsModel>?>?> =
        MutableStateFlow(State.Empty)

    val actorMovieCreditsValue: StateFlow<State<List<ActorMovieCreditsModel>?>?>
        get() = _actorMovieCreditsValue

    fun loadActorMovieCredits(actorId: Int?) {
        viewModelScope.launch(coroutineExceptionHandler) {
            getActorMovieCreditsUseCase.execute(actorId = actorId ?: 0).collect { movies ->
                _actorMovieCreditsValue.update { State.Success(movies) }
            }
        }
    }
}

class ActorMovieCreditsViewModelFactory @Inject constructor(
    private val useCase: GetActorMovieCreditsUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActorMovieCreditsViewModel::class.java)) {
            return ActorMovieCreditsViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
