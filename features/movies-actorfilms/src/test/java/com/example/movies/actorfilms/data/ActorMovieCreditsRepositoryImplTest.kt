package com.example.movies.actorfilms.data

import com.example.movies.api.MoviesApi
import com.example.movies.api.model.ActorMovieCreditsResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class ActorMovieCreditsRepositoryImplTest {

    private lateinit var repository: ActorMovieCreditsRepositoryImpl
    private val moviesApi: MoviesApi = mockk()

    @Before
    fun setup() {
        repository = ActorMovieCreditsRepositoryImpl(moviesApi)
    }

    @Test
    fun `getActorMoviesCredits returns data from api`() = runTest {
        // given
        val actorId = 123
        val expectedResponse = ActorMovieCreditsResponse(
            cast = emptyList(),
            crew = emptyList(),
            id = actorId
        )

        coEvery { moviesApi.getActorMoviesCredits(actorId) } returns expectedResponse

        // when
        val result = repository.getActorMoviesCredits(actorId)

        // then
        assertEquals(expectedResponse, result)
        coVerify(exactly = 1) { moviesApi.getActorMoviesCredits(actorId) }
    }

    @Test
    fun `getActorMoviesCredits with null id calls api with zero`() = runTest {
        // given
        val expectedResponse = ActorMovieCreditsResponse(
            cast = emptyList(),
            crew = emptyList(),
            id = 0
        )

        coEvery { moviesApi.getActorMoviesCredits(0) } returns expectedResponse

        // when
        val result = repository.getActorMoviesCredits(null)

        // then
        assertEquals(expectedResponse, result)
        coVerify(exactly = 1) { moviesApi.getActorMoviesCredits(0) }
    }
}
