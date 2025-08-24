package com.example.movies.auth.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class CreateRequestTokenUseCaseTest {

    private lateinit var repo: MoviesAuthRepository
    private lateinit var useCase: CreateRequestTokenUseCase

    @Before
    fun setup() {
        repo = mockk()
        useCase = CreateRequestTokenUseCase(repo)
    }

    @Test
    fun `execute should return request token from repository`() = runTest {
        // given
        val expectedToken = "test_token"
        coEvery { repo.createRequestToken() } returns expectedToken

        // when
        val result = useCase.execute()

        // then
        assertEquals(expectedToken, result)
        coVerify(exactly = 1) { repo.createRequestToken() }
    }
}
