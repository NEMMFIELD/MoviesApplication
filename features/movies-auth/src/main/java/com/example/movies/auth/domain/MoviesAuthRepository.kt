package com.example.movies.auth.domain

interface MoviesAuthRepository {
    suspend fun createRequestToken(): String
    suspend fun createSession(requestToken: String): String
    suspend fun saveSessionId(sessionId: String)
}
