package com.example.movies.auth.domain

import javax.inject.Inject

class CreateRequestTokenUseCase @Inject constructor(private val repo: MoviesAuthRepository) {
    suspend fun execute() = repo.createRequestToken()
}
