package com.example.movies.auth.ui

import com.example.movies.auth.domain.CreateRequestTokenUseCase
import com.example.movies.auth.domain.CreateSessionUseCase
import com.example.movies.auth.domain.GetSessionIdUseCase
import com.example.movies.auth.domain.SaveSessionIdUseCase
import com.example.state.State
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class MoviesAuthViewModelTest {

    private lateinit var createRequestToken: CreateRequestTokenUseCase
    private lateinit var createSession: CreateSessionUseCase
    private lateinit var saveSessionId: SaveSessionIdUseCase
    private lateinit var getSessionId: GetSessionIdUseCase
    private lateinit var viewModel: MoviesAuthViewModel
    private val testDispatcher = StandardTestDispatcher()
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        createRequestToken = mockk()
        createSession = mockk()
        saveSessionId = mockk()
        getSessionId = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should set sessionState when sessionId exists`() = runTest {
        // given
        val existingSession = "session_123"
        coEvery { getSessionId.execute() } returns existingSession

        // when
        viewModel = MoviesAuthViewModel(createRequestToken, createSession, saveSessionId, getSessionId)
        advanceUntilIdle()

        // then
        assertTrue(viewModel.sessionState is com.example.state.State.Success)
        assertEquals(existingSession, (viewModel.sessionState as com.example.state.State.Success).data)
    }

    @Test
    fun `startAuth should update tokenState to Success`() = runTest {
        // given
        val token = "token_abc"
        coEvery { getSessionId.execute() } returns null
        coEvery { createRequestToken.execute() } returns token
        viewModel = MoviesAuthViewModel(createRequestToken, createSession, saveSessionId, getSessionId)

        // when
        viewModel.startAuth()
        advanceUntilIdle()

        // then
        assertTrue(viewModel.tokenState is com.example.state.State.Success)
        assertEquals(token, (viewModel.tokenState as com.example.state.State.Success).data)
        assertEquals(false, viewModel.isLoading)
    }

    @Test
    fun `startAuth should update tokenState to Failure on error`() = runTest {
        // given
        val error = RuntimeException("network error")
        coEvery { getSessionId.execute() } returns null
        coEvery { createRequestToken.execute() } throws error
        viewModel = MoviesAuthViewModel(createRequestToken, createSession, saveSessionId, getSessionId)

        // when
        viewModel.startAuth()
        advanceUntilIdle()

        // then
        assertTrue(viewModel.tokenState is com.example.state.State.Failure)
        assertEquals(error, (viewModel.tokenState as com.example.state.State.Failure).message)
    }

    @Test
    fun `finishAuth should save session and update sessionState`() = runTest {
        // given
        val token = "token_abc"
        val sessionId = "session_123"
        coEvery { getSessionId.execute() } returns null
        coEvery { createSession.execute(token) } returns sessionId
        coEvery { saveSessionId.execute(sessionId) } returns Unit
        viewModel = MoviesAuthViewModel(createRequestToken, createSession, saveSessionId, getSessionId)

        // when
        viewModel.finishAuth(token)
        advanceUntilIdle()

        // then
        assertTrue(viewModel.sessionState is com.example.state.State.Success)
        assertEquals(sessionId, (viewModel.sessionState as com.example.state.State.Success).data)

        coVerify { saveSessionId.execute(sessionId) }
    }

    @Test
    fun `finishAuth should update sessionState to Failure on error`() = runTest {
        // given
        val token = "token_abc"
        val error = RuntimeException("bad session")
        coEvery { getSessionId.execute() } returns null
        coEvery { createSession.execute(token) } throws error
        viewModel = MoviesAuthViewModel(createRequestToken, createSession, saveSessionId, getSessionId)

        // when
        viewModel.finishAuth(token)
        advanceUntilIdle()

        // then
        assertTrue(viewModel.sessionState is com.example.state.State.Failure)
        assertEquals(error, (viewModel.sessionState as State.Failure).message)
    }
}
