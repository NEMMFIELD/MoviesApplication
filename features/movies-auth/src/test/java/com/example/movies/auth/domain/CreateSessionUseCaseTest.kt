package com.example.movies.auth.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class CreateSessionUseCaseTest {

    private lateinit var repo: MoviesAuthRepository
    private lateinit var useCase: CreateSessionUseCase

    @Before
    fun setup() {
        repo = mockk()
        useCase = CreateSessionUseCase(repo)
    }

    @Test
    fun `execute should return session id from repository`() = runTest {
        val requestToken = "token_123"
        val expectedSessionId = "session_456"
        coEvery { repo.createSession(requestToken) } returns expectedSessionId

        val result = useCase.execute(requestToken)

        assertEquals(expectedSessionId, result)
        coVerify(exactly = 1) { repo.createSession(requestToken) }
    }
}
