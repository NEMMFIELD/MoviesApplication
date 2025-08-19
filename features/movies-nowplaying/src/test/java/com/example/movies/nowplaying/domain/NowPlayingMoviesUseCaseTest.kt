package com.example.movies.nowplaying.domain

import com.example.core_model.MovieModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
class NowPlayingMoviesUseCaseTest {

    private lateinit var repository: NowPlayingRepository
    private lateinit var useCase: NowPlayingMoviesUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        repository = mockk()
        useCase = NowPlayingMoviesUseCase(repository)
    }

    @Test
    fun `execute should emit movies from repository`() = runTest(testDispatcher) {
        // Arrange
        val movies = listOf(
            MovieModel(
                id = 1,
                title = "Test Movie",
                posterPath = "/poster.jpg",
               rating = 8.0f
            )
        )
        coEvery { repository.getNowPlayingMovies(page = 1) } returns movies

        // Act
        val result = useCase.execute(1).first()

        // Assert
        assertEquals(movies, result)
        coVerify { repository.getNowPlayingMovies(page = 1) }
    }

    @Test
    fun `execute should emit null when repository returns null`() = runTest(testDispatcher) {
        // Arrange
        coEvery { repository.getNowPlayingMovies(page = 2) } returns null

        // Act
        val result = useCase.execute(2).first()

        // Assert
        assertEquals(null, result)
        coVerify { repository.getNowPlayingMovies(page = 2) }
    }
}
