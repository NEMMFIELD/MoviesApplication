package com.example.movies.auth.domain

import javax.inject.Inject

class GetSessionIdUseCase @Inject constructor(private val authRepository: MoviesAuthRepository) {
    fun execute():String? = authRepository.getSessionId()
}
