package com.example.movies.api.model

// Для авторизации
data class RequestTokenResponse(
    val success: Boolean,
    val expires_at: String,
    val request_token: String
)

data class CreateSessionRequest(
    val request_token: String
)

data class CreateSessionResponse(
    val success: Boolean,
    val session_id: String
)

// Для рейтинга
data class RatingRequest(
    val value: Double
)


data class RatingResponse(
    val status_code: Int,
    val status_message: String
)
