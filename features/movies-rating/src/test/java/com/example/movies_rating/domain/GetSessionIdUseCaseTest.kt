package com.example.movies_rating.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class GetSessionIdUseCaseTest {

    private lateinit var repository: MoviesRatingRepository
    private lateinit var useCase: GetSessionIdUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetSessionIdUseCase(repository)
    }

    @Test
    fun `execute should return sessionId when available`() {
        // given
        every { repository.getSessionId() } returns "abc123"

        // when
        val result = useCase.execute()

        // then
        assertEquals("abc123", result)
        verify { repository.getSessionId() }
    }

    @Test
    fun `execute should return null when sessionId is not available`() {
        // given
        every { repository.getSessionId() } returns null

        // when
        val result = useCase.execute()

        // then
        assertNull(result)
        verify { repository.getSessionId() }
    }
}
