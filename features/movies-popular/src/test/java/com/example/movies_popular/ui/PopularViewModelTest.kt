package com.example.movies_popular.ui

import com.example.core_model.MovieModel
import com.example.movies_popular.domain.GetPopularMoviesUseCase
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


class PopularViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var popularViewModel : PopularViewModel
    private val useCase: GetPopularMoviesUseCase = mockk()

    private val testPopularMovies = listOf(
        MovieModel(id = 1, title = "Spider-Man", posterPath = "/poster.jpeg", rating = 7.5f),
        MovieModel(id=2, title = "Superman", posterPath = "/supra.jpeg", rating = 9.0f)
    )

    @Before
    fun setUp() {
        coEvery { useCase.execute(page = any()) } returns flowOf(testPopularMovies)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `first load success updates state and increments page`() = runTest {
        popularViewModel = PopularViewModel(useCase)

        advanceUntilIdle()

        assert(popularViewModel.popularMoviesValue.value is State.Success)
        val success = popularViewModel.popularMoviesValue.value as State.Success

        assertEquals(testPopularMovies,success.data)
        assertEquals(2,popularViewModel.currentPage)
        assertFalse (popularViewModel.isFirstLoad)
        assertFalse(popularViewModel.isLastPage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `empty result sets isLastPage`() = runTest {
        coEvery { useCase.execute(any()) } returns flowOf(emptyList())
        popularViewModel = PopularViewModel(useCase)
        advanceUntilIdle()
        assertTrue(popularViewModel.isLastPage)
        assertEquals(State.Empty,popularViewModel.popularMoviesValue.value)

    }


    @Test
    fun `exception sets Failure state`() = runTest {
        val exception = RuntimeException("Test error")
        coEvery { useCase.execute(any()) } throws exception

        popularViewModel = PopularViewModel(useCase)
        advanceUntilIdle()

        assert(popularViewModel.popularMoviesValue.value is State.Failure)
        val failure = popularViewModel.popularMoviesValue.value as State.Failure
        Assert.assertEquals(exception, failure.message)
    }

    @Test
    fun `save and restore scroll position`() {
        popularViewModel = PopularViewModel(useCase)

        popularViewModel.saveScrollPosition(5, 100)
        Assert.assertEquals(5 to 100, popularViewModel.getScrollPosition())
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
