package com.example.movies.auth.data

import android.content.SharedPreferences
import com.example.movies.api.MoviesApi
import com.example.movies.api.model.CreateSessionRequest
import com.example.movies.api.model.CreateSessionResponse
import com.example.movies.api.model.RequestTokenResponse
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class MoviesAuthRepositoryImplTest {

    private lateinit var moviesApi: MoviesApi
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var repository: MoviesAuthRepositoryImpl

    @Before
    fun setup() {
        moviesApi = mockk()
        sharedPreferences = mockk()
        editor = mockk()

        every { sharedPreferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.apply() } just Runs

        repository = MoviesAuthRepositoryImpl(moviesApi, sharedPreferences)
    }

    @Test
    fun `createRequestToken should return token from api`() = runBlocking {
        // given
        val expectedToken = "abc123"
        coEvery { moviesApi.getRequestToken() } returns RequestTokenResponse(true, request_token = expectedToken, expires_at = "2025-10-10")

        // when
        val result = repository.createRequestToken()

        // then
        assertEquals(expectedToken, result)
        coVerify { moviesApi.getRequestToken() }
    }

    @Test
    fun `createSession should return session id from api`() = runBlocking {
        // given
        val requestToken = "token123"
        val expectedSessionId = "session456"
        coEvery { moviesApi.createSession(CreateSessionRequest(requestToken)) } returns CreateSessionResponse(
            true, session_id = expectedSessionId
        )

        // when
        val result = repository.createSession(requestToken)

        // then
        assertEquals(expectedSessionId, result)
        coVerify { moviesApi.createSession(CreateSessionRequest(requestToken)) }
    }

    @Test
    fun `saveSessionId should save session id into shared preferences`() = runTest {
        // given
        val sessionId = "session789"
        every { editor.putString("session_id", sessionId) } returns editor
        every { editor.apply() } just Runs

        // when
        repository.saveSessionId(sessionId)

        // then
        coVerify { editor.putString("session_id", sessionId) }
        coVerify { editor.apply() }
    }

    @Test
    fun `getSessionId should return session id from shared preferences`() {
        // given
        val expectedSessionId = "storedSession"
        every { sharedPreferences.getString("session_id", null) } returns expectedSessionId

        // when
        val result = repository.getSessionId()

        // then
        assertEquals(expectedSessionId, result)
    }

    @Test
    fun `getSessionId should return null if no session stored`() {
        // given
        every { sharedPreferences.getString("session_id", null) } returns null

        // when
        val result = repository.getSessionId()

        // then
        assertNull(result)
    }
}
