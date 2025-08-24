package com.example.movies_details.domain

import com.example.movies.api.model.CrewItem
import com.example.movies.api.model.MovieActorsResponse
import com.example.movies_details.data.MovieDetailsMapper
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
class GetMovieActorsUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GetMovieActorsUseCase
    private lateinit var repository: MovieDetailsRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        useCase = GetMovieActorsUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `execute should emit mapped list of MovieActorsModel`() = runTest {
        // given
        val movieId = 101
        val fakeCrew = listOf(
            CrewItem(id = 1, name = "Leonardo DiCaprio", job = "Actor"),
            CrewItem(id = 2, name = "Joseph Gordon-Levitt", job = "Actor")
        )
        val fakeDtoResponse = MovieActorsResponse(crew = fakeCrew)

        val expectedModels = fakeCrew.map { MovieDetailsMapper.mapDtoActorsToModelActors(it) }

        coEvery { repository.getMovieActors(movieId) } returns fakeDtoResponse

        // when
        val result = useCase.execute(movieId).first()

        // then
        assertEquals(expectedModels, result)
    }

    @Test
    fun `execute should emit null when crew is null`() = runTest {
        // given
        val movieId = 102
        val fakeDtoResponse = MovieActorsResponse(crew = null)

        coEvery { repository.getMovieActors(movieId) } returns fakeDtoResponse

        // when
        val result = useCase.execute(movieId).first()

        // then
        assertEquals(null, result)
    }
}
