package com.example.movies_rating.domain

import javax.inject.Inject

class GetRatingStatusUseCase @Inject constructor(
    private val ratingRepository: MoviesRatingRepository
) {
    suspend fun execute(sessionId: String?, movieId: Int?): Boolean {
        val ratingStatus = ratingRepository.getRatingStatus(sessionId = sessionId, movieId = movieId)

        return when (val ratedValue = ratingStatus.rated) {
            is Boolean -> false // сервер прислал false → нет оценки
            is Map<*, *> -> true // сервер прислал объект {"value": ...} → есть оценка
            else -> false
        }
    }
}
