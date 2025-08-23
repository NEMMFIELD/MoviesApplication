package com.example.movies_upcoming.data

import com.example.movies.api.MoviesApi
import com.example.movies.api.model.DTOResponse
import com.example.movies.api.model.ResultsItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class UpcomingRepositoryImplTest {
    private lateinit var moviesApi: MoviesApi
    private lateinit var upComingRepository: UpcomingRepositoryImpl

    @Before
    fun setUp() {
        moviesApi = mockk(relaxed = true)
        upComingRepository = UpcomingRepositoryImpl(moviesApi)
    }

    @Test
    fun `getPopularMovies returns movies list`() = runTest {
        val dtoModel = ResultsItem(
            id = 14490,
            title = "The Texas Chainsaw Massacre",
            overview = "Horror",
            posterPath = "/massacre.jpeg",
            releaseDate = "2025-09-10"
        )
        val apiResponse = DTOResponse(
            results = listOf(dtoModel),
            page = 1,
            totalPages = 1,
            totalResults = 1
        )
        coEvery { moviesApi.getUpcomingMovies(page = 1) } returns apiResponse

        val result = upComingRepository.getUpcomingMovies(page = 1).results

        assertEquals(1,result?.size)
        assertEquals("The Texas Chainsaw Massacre",result?.first()?.title)
        coVerify { moviesApi.getUpcomingMovies(page = 1) }

    }
}
