package com.example.movies.actorfilms.ui

import app.cash.turbine.test
import com.example.core_ui.State
import com.example.movies.actorfilms.data.ActorMovieCreditsModel
import com.example.movies.actorfilms.domain.GetActorMovieCreditsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class ActorMovieCreditsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val useCase: GetActorMovieCreditsUseCase = mockk()
    private lateinit var viewModel: ActorMovieCreditsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ActorMovieCreditsViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadActorMovieCredits emits Success state`() = runTest {
        // given
        val actorId = 1
        val models = listOf(
            ActorMovieCreditsModel(id = 10, posterPath = "Director")
        )

        coEvery { useCase.execute(actorId) } returns flow { emit(models) }

        // when
        viewModel.actorMovieCreditsValue.test {
            assertTrue(awaitItem() is State.Empty) // начальное состояние

            viewModel.loadActorMovieCredits(actorId)
            testDispatcher.scheduler.advanceUntilIdle()

            val successState = awaitItem()
            assertTrue(successState is State.Success)
            assertTrue((successState as State.Success).data == models)
        }
    }

    @Test
    fun `loadActorMovieCredits emits Failure state on exception`() = runTest {
        // given
        val actorId = 1
        val exception = RuntimeException("Boom!")

        coEvery { useCase.execute(actorId) } returns flow { throw exception }

        // when
        viewModel.actorMovieCreditsValue.test {
            assertTrue(awaitItem() is com.example.core_ui.State.Empty)

            viewModel.loadActorMovieCredits(actorId)
            testDispatcher.scheduler.advanceUntilIdle()

            val failureState = awaitItem()
            assertTrue(failureState is State.Failure)

            val throwable = (failureState as State.Failure).message
            assertTrue(throwable is RuntimeException)
            assertTrue(throwable.message == "Boom!")
        }
    }
}
