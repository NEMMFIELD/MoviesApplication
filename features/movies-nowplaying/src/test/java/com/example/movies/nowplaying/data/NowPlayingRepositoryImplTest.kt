package com.example.movies.nowplaying.data

import com.example.movies.api.MoviesApi
import com.example.movies.api.model.DTOResponse
import com.example.movies.api.model.ResultsItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class NowPlayingRepositoryImplTest {

    private lateinit var moviesApi: MoviesApi
    private lateinit var nowPlayingRepository: NowPlayingRepositoryImpl

    @Before
    fun setUp() {
        moviesApi = mockk(relaxed = true)
        nowPlayingRepository = NowPlayingRepositoryImpl(moviesApi)
    }

    @Test
    fun `getNowPlayingMovies returns mapped movies list`() = runTest {
        // Arrange
        val dto = ResultsItem(
            id = 1,
            title = "Test Movie",
            overview = "Some overview",
            posterPath = "/poster.jpg",
            releaseDate = "2025-01-01"
        )
        val apiResponse = DTOResponse(
            results = listOf(dto),
            page = 1,
            totalPages = 1,
            totalResults = 1
        )
        coEvery { moviesApi.getNowPlayingMovies(page = 1) } returns apiResponse

        // Act
        val result = nowPlayingRepository.getNowPlayingMovies(1)

        // Assert
        assertEquals(1, result?.size)
        assertEquals("Test Movie", result?.first()?.title)

        coVerify { moviesApi.getNowPlayingMovies(page = 1) }
    }
}
