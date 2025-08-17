package com.example.movies.auth.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movies.auth.domain.CreateRequestTokenUseCase
import com.example.movies.auth.domain.CreateSessionUseCase
import com.example.movies.auth.domain.GetSessionIdUseCase
import com.example.movies.auth.domain.SaveSessionIdUseCase
import com.example.state.State
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesAuthViewModel @Inject constructor(
    private val createRequestToken: CreateRequestTokenUseCase,
    private val createSession: CreateSessionUseCase,
    private val saveSessionId: SaveSessionIdUseCase,
    private val getSessionIdUseCase: GetSessionIdUseCase
) : ViewModel() {
    var tokenState by mutableStateOf<com.example.state.State<String>>(com.example.state.State.Empty)
        private set

    var sessionState by mutableStateOf<com.example.state.State<String>>(State.Empty)
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            val savedSessionId = getSessionIdUseCase.execute()
            if (savedSessionId != null) {
                sessionState = State.Success(savedSessionId)
            }
        }
    }
    fun startAuth() {
        viewModelScope.launch {
            isLoading = true
            tokenState = com.example.state.State.Empty
            try {
                val token = createRequestToken.execute()
                tokenState = com.example.state.State.Success(token)
            } catch (e: Exception) {
                tokenState = com.example.state.State.Failure(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun finishAuth(token: String) {
        viewModelScope.launch {
            isLoading = true
            sessionState = com.example.state.State.Empty
            try {
                val sessionId = createSession.execute(token)
                saveSessionId.execute(sessionId)
                sessionState = com.example.state.State.Success(sessionId)
            } catch (e: Exception) {
                sessionState = com.example.state.State.Failure(e)
            } finally {
                isLoading = false
            }
        }
    }
}

class MoviesAuthViewModelFactory @Inject constructor(
    private val createRequestToken: CreateRequestTokenUseCase,
    private val createSession: CreateSessionUseCase,
    private val saveSessionId: SaveSessionIdUseCase,
    private val getSessionIdUseCase: GetSessionIdUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesAuthViewModel::class.java)) {
            return MoviesAuthViewModel(createRequestToken, createSession, saveSessionId,getSessionIdUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
