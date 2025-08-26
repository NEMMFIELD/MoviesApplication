package com.example.movies_rating.domain

import com.example.movies.api.model.RatingResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class RateMoviesUseCaseTest {

    private lateinit var repository: MoviesRatingRepository
    private lateinit var useCase: RateMoviesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = RateMoviesUseCase(repository)
    }

    @Test
    fun `execute should return RatingResponse from repository`() = runBlocking {
        // given
        val movieId = 100
        val rating = 9.0
        val sessionId = "session_123"
        val expectedResponse = RatingResponse(123,"")

        coEvery { repository.rateMovie(sessionId, movieId, rating) } returns expectedResponse

        // when
        val result = useCase.execute(movieId, rating, sessionId)

        // then
        assertEquals(expectedResponse, result)
        coVerify { repository.rateMovie(sessionId, movieId, rating) }
    }
}
