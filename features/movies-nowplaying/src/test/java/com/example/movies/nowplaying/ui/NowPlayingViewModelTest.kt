package com.example.movies.nowplaying.ui

import com.example.core_model.MovieModel
import com.example.movies.nowplaying.domain.NowPlayingMoviesUseCase
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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class NowPlayingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: NowPlayingViewModel
    private val useCase: NowPlayingMoviesUseCase = mockk()

    private val testMovies = listOf(
        MovieModel(id = 1, title = "Movie 1","bzhome.com",7.0f),
        MovieModel(id = 2, title = "Movie 2","lenta.ru", 6.5f)
    )

    @Before
    fun setup() {
        // useCase по умолчанию возвращает список фильмов
        coEvery { useCase.execute(any()) } returns flowOf(testMovies)
    }

    @Test
    fun `first load success updates state and increments page`() = runTest {
        viewModel = NowPlayingViewModel(useCase)

        advanceUntilIdle() // ждем завершения launch

        assert(viewModel.nowPlayingMoviesValue.value is com.example.state.State.Success)
        val success = viewModel.nowPlayingMoviesValue.value as com.example.state.State.Success
        assertEquals(testMovies, success.data)

        assertEquals(2, viewModel.currentPage)
        assertFalse(viewModel.isFirstLoad)
        assertFalse(viewModel.isLastPage)
    }

    @Test
    fun `empty result sets isLastPage`() = runTest {
        coEvery { useCase.execute(any()) } returns flowOf(emptyList())

        viewModel = NowPlayingViewModel(useCase)
        advanceUntilIdle()

        assertTrue(viewModel.isLastPage)
        assertEquals(com.example.state.State.Empty, viewModel.nowPlayingMoviesValue.value)
    }

    @Test
    fun `exception sets Failure state`() = runTest {
        val exception = RuntimeException("Test error")
        coEvery { useCase.execute(any()) } throws exception

        viewModel = NowPlayingViewModel(useCase)
        advanceUntilIdle()

        assert(viewModel.nowPlayingMoviesValue.value is com.example.state.State.Failure)
        val failure = viewModel.nowPlayingMoviesValue.value as com.example.state.State.Failure
        assertEquals(exception, failure.message)
    }

    @Test
    fun `save and restore scroll position`() {
        viewModel = NowPlayingViewModel(useCase)

        viewModel.saveScrollPosition(5, 100)
        assertEquals(5 to 100, viewModel.getScrollPosition())
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
