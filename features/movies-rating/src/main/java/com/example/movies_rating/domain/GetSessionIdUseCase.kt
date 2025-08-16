package com.example.movies_rating.domain

import javax.inject.Inject

class GetSessionIdUseCase @Inject constructor(private val repository: MoviesRatingRepository) {
    fun execute():String? = repository.getSessionId()
}
