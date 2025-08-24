package com.example.movies.toprated.ui

import com.example.core_model.MovieModel
import com.example.movies.toprated.domain.GetTopRatedMoviesUseCase
import com.example.core_ui.State
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


class TopRatedViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var topRatedViewModel : TopRatedViewModel
    private val useCase: GetTopRatedMoviesUseCase = mockk()

    private val testTopRatedMovies = listOf(
        MovieModel(id = 1, title = "Avengers", posterPath = "/poster.jpeg", rating = 7.5f),
        MovieModel(id=2, title = "Hulk", posterPath = "/hulk.jpeg", rating = 8.0f)
    )

    @Before
    fun setUp() {
        coEvery { useCase.execute(page = any()) } returns flowOf(testTopRatedMovies)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `first load success updates state and increments page`() = runTest {
        topRatedViewModel = TopRatedViewModel(useCase)

        advanceUntilIdle()

        assert(topRatedViewModel.topRatedMoviesValue.value is State.Success)
        val success = topRatedViewModel.topRatedMoviesValue.value as State.Success

        assertEquals(testTopRatedMovies,success.data)
        assertEquals(2,topRatedViewModel.currentPage)
        assertTrue (topRatedViewModel.isFirstLoad)
        assertFalse(topRatedViewModel.isLastPage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `empty result sets isLastPage`() = runTest {
        coEvery { useCase.execute(any()) } returns flowOf(emptyList())
        topRatedViewModel = TopRatedViewModel(useCase)
        advanceUntilIdle()
        assertTrue(topRatedViewModel.isLastPage)
        assertEquals(State.Empty,topRatedViewModel.topRatedMoviesValue.value)

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `exception sets Failure state`() = runTest {
        val exception = RuntimeException("Test error")
        coEvery { useCase.execute(any()) } throws exception

        topRatedViewModel = TopRatedViewModel(useCase)
        advanceUntilIdle()

        assert(topRatedViewModel.topRatedMoviesValue.value is State.Failure)
        val failure = topRatedViewModel.topRatedMoviesValue.value as State.Failure
        Assert.assertEquals(exception, failure.message)
    }

    @Test
    fun `save and restore scroll position`() {
        topRatedViewModel = TopRatedViewModel(useCase)

        topRatedViewModel.saveScrollPosition(5, 100)
        Assert.assertEquals(5 to 100, topRatedViewModel.getScrollPosition())
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
