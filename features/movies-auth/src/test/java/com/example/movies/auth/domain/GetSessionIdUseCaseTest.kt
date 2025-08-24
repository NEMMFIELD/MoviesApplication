package com.example.movies.auth.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class GetSessionIdUseCaseTest {

    private lateinit var repo: MoviesAuthRepository
    private lateinit var useCase: GetSessionIdUseCase

    @Before
    fun setup() {
        repo = mockk()
        useCase = GetSessionIdUseCase(repo)
    }

    @Test
    fun `execute should return session id from repository`() {
        // given
        val expectedSessionId = "session_123"
        every { repo.getSessionId() } returns expectedSessionId

        // when
        val result = useCase.execute()

        // then
        assertEquals(expectedSessionId, result)
        verify(exactly = 1) { repo.getSessionId() }
    }

    @Test
    fun `execute should return null if repository has no session id`() {
        // given
        every { repo.getSessionId() } returns null

        // when
        val result = useCase.execute()

        // then
        assertNull(result)
        verify(exactly = 1) { repo.getSessionId() }
    }
}
