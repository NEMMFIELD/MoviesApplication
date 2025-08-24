package com.example.movies_details.ui

import androidx.lifecycle.SavedStateHandle
import com.example.core_ui.State
import com.example.movies_details.data.MovieActorsModel
import com.example.movies_details.data.MovieDetailsModel
import com.example.movies_details.domain.GetMovieActorsUseCase
import com.example.movies_details.domain.GetMovieDetailsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var movieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var movieActorsUseCase: GetMovieActorsUseCase
    private lateinit var viewModel: MovieDetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        movieDetailsUseCase = mockk()
        movieActorsUseCase = mockk()
        viewModel = MovieDetailsViewModel(
            movieDetailsUseCase = movieDetailsUseCase,
            movieActorsUseCase = movieActorsUseCase,
            savedStateHandle = SavedStateHandle()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMovieDetails should update movieDetailsValue with Success`() = runTest {
        // given
        val movieId = 1
        val fakeMovieDetails = MovieDetailsModel(
            id = 1,
            title = "Inception",
            backdropPath = "/backdrop.jpg",
            overview = "Dream within a dream",
            rating = 7.0f,
            genres = listOf()
        )

        coEvery { movieDetailsUseCase.execute(movieId) } returns flowOf(fakeMovieDetails)

        // when
        viewModel.loadMovieDetails(movieId)
        advanceUntilIdle() // дождаться завершения корутин

        // then
        val state = viewModel.movieDetailsValue.value
        assertEquals(com.example.core_ui.State.Success(fakeMovieDetails), state)
    }

    @Test
    fun `loadMovieActors should update movieDetailsActors with Success`() = runTest {
        // given
        val movieId = 1
        val fakeActors = listOf(
            MovieActorsModel(id = 1, name = "Leonardo DiCaprio", profilePath = "Actor"),
            MovieActorsModel(id = 2, name = "Joseph Gordon-Levitt", profilePath = "Actor")
        )

        coEvery { movieActorsUseCase.execute(movieId) } returns flowOf(fakeActors)

        // when
        viewModel.loadMovieActors(movieId)
        advanceUntilIdle()

        // then
        val state = viewModel.movieDetailsActors.value
        assertEquals(State.Success(fakeActors), state)
    }
}
