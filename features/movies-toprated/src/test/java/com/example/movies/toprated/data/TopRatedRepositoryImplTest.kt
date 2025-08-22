package com.example.movies.toprated.data

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


class TopRatedRepositoryImplTest {
    private lateinit var moviesApi: MoviesApi
    private lateinit var topRatedRepository: TopRatedRepositoryImpl

    @Before
    fun setUp() {
        moviesApi = mockk(relaxed = true)
        topRatedRepository = TopRatedRepositoryImpl(moviesApi)
    }

    @Test
    fun `getTopRatedMovies returns movies list`() = runTest {
        val dtoModel = ResultsItem(
            id = 14495,
            title = "LOST",
            overview = "Show",
            posterPath = "/lost.jpeg",
            releaseDate = "2022-08-25"
        )
        val apiResponse = DTOResponse(
            results = listOf(dtoModel),
            page = 1,
            totalPages = 1,
            totalResults = 1
        )
        coEvery { moviesApi.getTopRatedMovies(page = 1) } returns apiResponse

        val result = topRatedRepository.loadTopRatedMovies(page = 1).results

        assertEquals(1,result?.size)
        assertEquals("LOST",result?.first()?.title)
        coVerify { moviesApi.getTopRatedMovies(page = 1) }

    }

}
