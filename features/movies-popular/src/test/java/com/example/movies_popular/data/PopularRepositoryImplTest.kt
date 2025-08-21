package com.example.movies_popular.data

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


class PopularRepositoryImplTest {
    private lateinit var moviesApi: MoviesApi
    private lateinit var popularRepository: PopularRepositoryImpl

    @Before
    fun setUp() {
        moviesApi = mockk(relaxed = true)
        popularRepository = PopularRepositoryImpl(moviesApi)
    }

    @Test
    fun `getPopularMovies returns movies list`() = runTest {
        val dtoModel = ResultsItem(
            id = 14495,
            title = "Dracone",
            overview = "Fantastic drama",
            posterPath = "/dracone.jpeg",
            releaseDate = "2025-08-25"
        )
        val apiResponse = DTOResponse(
            results = listOf(dtoModel),
            page = 1,
            totalPages = 1,
            totalResults = 1
        )
        coEvery { moviesApi.getPopularMovies(page = 1) } returns apiResponse

        val result = popularRepository.loadPopularMovies(page = 1).results

        assertEquals(1,result?.size)
        assertEquals("Dracone",result?.first()?.title)
        coVerify { moviesApi.getPopularMovies(page = 1) }

    }

}
