package com.example.movies.toprated.domain

import com.example.core_model.MovieMapper
import com.example.movies.api.model.DTOResponse
import com.example.movies.api.model.ResultsItem
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GetTopRatedMoviesUseCaseTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GetTopRatedMoviesUseCase
    private val repository: TopRatedRepository = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = GetTopRatedMoviesUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `execute should return mapped movie models`() = runTest {
        val dtoItem = ResultsItem(
            id = 1,
            title = "Test movie",
            posterPath = "/test.jpg",
            voteAverage = 8.0F
        )

        val responseDto = DTOResponse(results = listOf(dtoItem))
        coEvery { repository.loadTopRatedMovies(1) } returns responseDto

        val result = useCase.execute(1).first()

        val expected = listOf(MovieMapper.mapDtoToModel(dtoItem))
        assertEquals(expected, result)
    }

    @Test
    fun `execute should return empty list when results is null`() = runTest {

        val responseDto = DTOResponse(results = null)
        coEvery { repository.loadTopRatedMovies(1) } returns responseDto

        val result = useCase.execute(1).first()

        assertEquals(null, result)
    }
}
