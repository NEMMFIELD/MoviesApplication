package com.example.movies_upcoming.ui

import com.example.core_model.MovieModel
import com.example.movies_upcoming.domain.GetUpcomingMoviesUseCase
import com.example.state.State
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class UpcomingViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var upcomingViewModel : UpcomingViewModel
    private val useCase: GetUpcomingMoviesUseCase = mockk()

    private val testUpcomingMovies = listOf(
        MovieModel(id = 1, title = "Blade", posterPath = "/poster.jpeg", rating = 5.5f),
        MovieModel(id=2, title = "Inferno", posterPath = "/inferno.jpeg", rating = 3.0f)
    )

    @Before
    fun setUp() {
        coEvery { useCase.execute(page = any()) } returns flowOf(testUpcomingMovies)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `first load success updates state and increments page`() = runTest {
        upcomingViewModel = UpcomingViewModel(useCase)

        advanceUntilIdle()

        assert(upcomingViewModel.upcomingMoviesValue.value is State.Success)
        val success = upcomingViewModel.upcomingMoviesValue.value as State.Success

        assertEquals(testUpcomingMovies, success.data)
        assertEquals(2, upcomingViewModel.currentPage)
        assertTrue (upcomingViewModel.isFirstLoad)
        assertFalse(upcomingViewModel.isLastPage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `empty result sets isLastPage`() = runTest {
        coEvery { useCase.execute(any()) } returns flowOf(emptyList())
        upcomingViewModel = UpcomingViewModel(useCase)
        advanceUntilIdle()
        assertTrue(upcomingViewModel.isLastPage)
        assertEquals(State.Empty, upcomingViewModel.upcomingMoviesValue.value)

    }


    @Test
    fun `exception sets Failure state`() = runTest {
        val exception = RuntimeException("Test error")
        coEvery { useCase.execute(any()) } throws exception

        upcomingViewModel = UpcomingViewModel(useCase)
        advanceUntilIdle()

        assert(upcomingViewModel.upcomingMoviesValue.value is State.Failure)
        val failure = upcomingViewModel.upcomingMoviesValue.value as State.Failure
        Assert.assertEquals(exception, failure.message)
    }

    @Test
    fun `save and restore scroll position`() {
        upcomingViewModel = UpcomingViewModel(useCase)

        upcomingViewModel.saveScrollPosition(5, 100)
        Assert.assertEquals(5 to 100, upcomingViewModel.getScrollPosition())
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: org.junit.runner.Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: org.junit.runner.Description) {
        Dispatchers.resetMain()
    }
}
