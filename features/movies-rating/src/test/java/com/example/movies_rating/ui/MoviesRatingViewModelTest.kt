package com.example.movies_rating.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.movies.api.model.RatingResponse
import com.example.movies_rating.data.MoviesRatingUiState
import com.example.movies_rating.domain.GetRatingStatusUseCase
import com.example.movies_rating.domain.GetSessionIdUseCase
import com.example.movies_rating.domain.RateMoviesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class MoviesRatingViewModelTest {

    @get:Rule
    val instantExecutorRule =
        InstantTaskExecutorRule() // для LiveData, StateFlow
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var rateMoviesUseCase: RateMoviesUseCase
    private lateinit var getSessionIdUseCase: GetSessionIdUseCase
    private lateinit var getRatingStatusUseCase: GetRatingStatusUseCase
    private lateinit var viewModel: MoviesRatingViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        rateMoviesUseCase = mockk()
        getSessionIdUseCase = mockk()
        getRatingStatusUseCase = mockk()
        viewModel = MoviesRatingViewModel(rateMoviesUseCase, getSessionIdUseCase, getRatingStatusUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `rateMovie should emit Loading and MovieRated when session exists`() = runTest {

        val movieId = 1
        val rating = 8.0
        val sessionId = "abc123"
        val ratingResponse = RatingResponse(123,"OK")

        coEvery { getSessionIdUseCase.execute() } returns sessionId
        coEvery { rateMoviesUseCase.execute(movieId, rating, sessionId) } returns ratingResponse


        viewModel.rateMovie(movieId, rating)
        advanceUntilIdle() // чтобы все coroutines завершились


        val uiState = viewModel.uiState.first()
        assert(uiState is MoviesRatingUiState.MovieRated)
        assertEquals(ratingResponse, (uiState as MoviesRatingUiState.MovieRated).response)
    }

    @Test
    fun `rateMovie emits Error when session is null`() = runTest {
        // given
        coEvery { getSessionIdUseCase.execute() } returns null

        // when & then
        viewModel.uiState.test {
            viewModel.rateMovie(1, 8.0)

            assertEquals(MoviesRatingUiState.Idle, awaitItem())
            assertEquals(MoviesRatingUiState.Loading, awaitItem())

            val errorState = awaitItem()
            assertTrue(errorState is MoviesRatingUiState.Error)
            assertEquals("No session ID", (errorState as MoviesRatingUiState.Error).throwable.message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadRatingStatus should emit Loading and RatingStatusLoaded when session exists`() = runTest {
        val movieId = 1
        val sessionId = "abc123"
        coEvery { getSessionIdUseCase.execute() } returns sessionId
        coEvery { getRatingStatusUseCase.execute(sessionId, movieId) } returns true

        viewModel.loadRatingStatus(movieId)
        advanceUntilIdle()


        val uiState = viewModel.uiState.first()
        assert(uiState is MoviesRatingUiState.RatingStatusLoaded)
        assertEquals(true, (uiState as MoviesRatingUiState.RatingStatusLoaded).status)
    }

    @Test
    fun `loadRatingStatus should emit Error when session is null`() = runTest {
        coEvery { getSessionIdUseCase.execute() } returns null

        viewModel.uiState.test {
            viewModel.loadRatingStatus(1)

            assertEquals(MoviesRatingUiState.Idle, awaitItem())
            assertEquals(MoviesRatingUiState.Loading, awaitItem())

            val errorState = awaitItem()
            assertTrue(errorState is MoviesRatingUiState.Error)
            assertEquals(
                "No session ID",
                (errorState as MoviesRatingUiState.Error).throwable.message
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}
