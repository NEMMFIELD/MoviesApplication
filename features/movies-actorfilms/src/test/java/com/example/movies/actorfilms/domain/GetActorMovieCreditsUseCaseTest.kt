package com.example.movies.actorfilms.domain

import com.example.movies.actorfilms.data.ActorMovieCreditsMapper
import com.example.movies.api.model.ActorCrewItem
import com.example.movies.api.model.ActorMovieCreditsResponse
import com.example.movies.api.model.CrewItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class GetActorMovieCreditsUseCaseTest {

    private lateinit var useCase: GetActorMovieCreditsUseCase
    private val repository: ActorMovieCreditsRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = GetActorMovieCreditsUseCase(repository)
    }

    @Test
    fun `execute returns mapped crew list`() = runTest {
        // given
        val actorId = 42
        val crewDto = ActorCrewItem(id = 1, job = "Director", title = "Test Movie") // пример DTO
        val response = ActorMovieCreditsResponse(
            cast = emptyList(),
            crew = listOf(crewDto),
            id = actorId
        )
        val expectedModel = ActorMovieCreditsMapper.mapDtoToActorMoviesCreditsModel(crewDto)

        coEvery { repository.getActorMoviesCredits(actorId) } returns response

        // when
        val result = useCase.execute(actorId).first()

        // then
        assertEquals(listOf(expectedModel), result)
        coVerify(exactly = 1) { repository.getActorMoviesCredits(actorId) }
    }

    @Test
    fun `execute with null crew emits null`() = runTest {
        // given
        val actorId = 42
        val response = ActorMovieCreditsResponse(
            cast = emptyList(),
            crew = null,
            id = actorId
        )

        coEvery { repository.getActorMoviesCredits(actorId) } returns response

        // when
        val result = useCase.execute(actorId).first()

        // then
        assertEquals(null, result)
        coVerify(exactly = 1) { repository.getActorMoviesCredits(actorId) }
    }
}
