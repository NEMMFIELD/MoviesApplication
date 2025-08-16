package com.example.movies.auth.domain

import javax.inject.Inject

class SaveSessionIdUseCase @Inject constructor(private val repo: MoviesAuthRepository) {
    suspend fun execute(sessionId: String) = repo.saveSessionId(sessionId)
}
