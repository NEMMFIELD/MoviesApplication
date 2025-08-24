package com.example.movies_details.domain

import com.example.movies.api.model.MovieDetailsResponse
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
class GetMovieDetailsUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GetMovieDetailsUseCase
    private lateinit var repository: MovieDetailsRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        useCase = GetMovieDetailsUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `execute should emit mapped MovieDetailsModel`() = runTest {
        // given
        val movieId = 42
        val fakeDto = MovieDetailsResponse(
            id = 42,
            title = "Inception",
            backdropPath = "/backdrop.jpg",
            overview = "Dream within a dream"
        )
        val expectedModel = MovieDetailsMapper.mapDtoToModel(fakeDto)

        coEvery { repository.getMovieDetails(movieId) } returns fakeDto

        // when
        val result = useCase.execute(movieId).first()

        // then
        assertEquals(expectedModel, result)
    }
}
