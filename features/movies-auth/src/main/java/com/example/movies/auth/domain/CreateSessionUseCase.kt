package com.example.movies.auth.domain

import javax.inject.Inject

class CreateSessionUseCase @Inject constructor(private val repo: MoviesAuthRepository) {
    suspend fun execute(requestToken: String) = repo.createSession(requestToken)
}
