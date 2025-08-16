package com.example.movies.auth.data

import android.content.SharedPreferences
import com.example.movies.api.MoviesApi
import com.example.movies.api.model.CreateSessionRequest
import com.example.movies.auth.domain.MoviesAuthRepository
import javax.inject.Inject

class MoviesAuthRepositoryImpl @Inject constructor(private val moviesApi: MoviesApi, private val sharedPreferences: SharedPreferences): MoviesAuthRepository {
    override suspend fun createRequestToken(): String {
        return moviesApi.getRequestToken().request_token
    }

    override suspend fun createSession(requestToken: String): String {
        return moviesApi.createSession(CreateSessionRequest(requestToken)).session_id
    }

    override suspend fun saveSessionId(sessionId: String) {
        sharedPreferences.edit().putString("session_id",sessionId).apply()
    }

}
