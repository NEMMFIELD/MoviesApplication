package com.example.movies_rating.domain

import com.example.movies.api.model.RatingStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class GetRatingStatusUseCaseTest {

    private lateinit var repository: MoviesRatingRepository
    private lateinit var useCase: GetRatingStatusUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetRatingStatusUseCase(repository)
    }

    @Test
    fun `execute should return true when rated is Map`() = runBlocking {
        // given
        val sessionId = "abc123"
        val movieId = 42
        val ratingStatus = RatingStatus(
            id = movieId,
            favorite = false,
            watchlist = false,
            rated = mapOf("value" to 8.0)
        )

        coEvery { repository.getRatingStatus(sessionId, movieId) } returns ratingStatus

        // when
        val result = useCase.execute(sessionId, movieId)

        // then
        assertTrue(result)
        coVerify { repository.getRatingStatus(sessionId, movieId) }
    }

    @Test
    fun `execute should return false when rated is Boolean`() = runBlocking {
        // given
        val sessionId = "abc123"
        val movieId = 42
        val ratingStatus = RatingStatus(
            id = movieId,
            favorite = false,
            watchlist = false,
            rated = false
        )

        coEvery { repository.getRatingStatus(sessionId, movieId) } returns ratingStatus

        // when
        val result = useCase.execute(sessionId, movieId)

        // then
        assertFalse(result)
        coVerify { repository.getRatingStatus(sessionId, movieId) }
    }

    @Test
    fun `execute should return false when rated is null`() = runBlocking {
        // given
        val sessionId = "abc123"
        val movieId = 42
        val ratingStatus = RatingStatus(
            id = movieId,
            favorite = false,
            watchlist = false,
            rated = null
        )

        coEvery { repository.getRatingStatus(sessionId, movieId) } returns ratingStatus

        // when
        val result = useCase.execute(sessionId, movieId)

        // then
        assertFalse(result)
        coVerify { repository.getRatingStatus(sessionId, movieId) }
    }
}
