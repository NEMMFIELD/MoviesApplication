package com.example.movies.auth.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test


class SaveSessionIdUseCaseTest {

    private lateinit var repo: MoviesAuthRepository
    private lateinit var useCase: SaveSessionIdUseCase

    @Before
    fun setup() {
        repo = mockk()
        useCase = SaveSessionIdUseCase(repo)
    }

    @Test
    fun `execute should call repository to save session id`() = runTest {
        // given
        val sessionId = "session_123"
        coEvery { repo.saveSessionId(sessionId) } returns Unit

        // when
        useCase.execute(sessionId)

        // then
        coVerify(exactly = 1) { repo.saveSessionId(sessionId) }
    }
}
