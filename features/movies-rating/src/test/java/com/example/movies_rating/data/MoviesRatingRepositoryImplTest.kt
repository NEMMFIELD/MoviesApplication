package com.example.movies_rating.data

import android.content.SharedPreferences
import com.example.movies.api.MoviesApi
import com.example.movies.api.model.RatingRequest
import com.example.movies.api.model.RatingResponse
import com.example.movies.api.model.RatingStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class MoviesRatingRepositoryImplTest {

    private lateinit var moviesApi: MoviesApi
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var repository: MoviesRatingRepositoryImpl

    @Before
    fun setup() {
        moviesApi = mockk()
        sharedPreferences = mockk()
        repository = MoviesRatingRepositoryImpl(moviesApi, sharedPreferences)
    }

    @Test
    fun `rateMovie should call api and return response`() = runBlocking {
        // given
        val movieId = 123
        val sessionId = "abc123"
        val rating = 8.5
        val expectedResponse = RatingResponse(status_code =451, "ok")

        coEvery {
            moviesApi.addRating(movieId, sessionId, RatingRequest(rating))
        } returns expectedResponse

        // when
        val result = repository.rateMovie(sessionId, movieId, rating)

        // then
        assertEquals(expectedResponse, result)
        coVerify { moviesApi.addRating(movieId, sessionId, RatingRequest(rating)) }
    }

    @Test
    fun `getSessionId should return value from sharedPreferences`() {
        // given
        every { sharedPreferences.getString("session_id", null) } returns "my_session"

        // when
        val result = repository.getSessionId()

        // then
        assertEquals("my_session", result)
        verify { sharedPreferences.getString("session_id", null) }
    }

    @Test
    fun `getSessionId should return null when not found`() {
        // given
        every { sharedPreferences.getString("session_id", null) } returns null

        // when
        val result = repository.getSessionId()

        // then
        assertNull(result)
        verify { sharedPreferences.getString("session_id", null) }
    }

    @Test
    fun `getRatingStatus should call api and return status`() = runBlocking {
        // given
        val movieId = 456
        val sessionId = "xyz789"
        val expectedStatus = RatingStatus(
            id = movieId,
            favorite = true,
            watchlist = false,
            rated = mapOf("value" to 8.0)
        )

        coEvery {
            moviesApi.getRatingStatus(movieId, sessionId)
        } returns expectedStatus

        // when
        val result = repository.getRatingStatus(sessionId, movieId)

        // then
        assertEquals(expectedStatus, result)
        coVerify { moviesApi.getRatingStatus(movieId, sessionId) }
    }
}
