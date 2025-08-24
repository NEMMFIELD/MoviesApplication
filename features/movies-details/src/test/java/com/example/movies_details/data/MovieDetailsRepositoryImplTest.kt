package com.example.movies_details.data

import com.example.movies.api.MoviesApi
import com.example.movies.api.model.CastItem
import com.example.movies.api.model.CrewItem
import com.example.movies.api.model.MovieActorsResponse
import com.example.movies.api.model.MovieDetailsResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


val fakeMovieDetailsResponse = MovieDetailsResponse(
    id = 1,
    title = "Fake Movie",
    overview = "Overview",
    releaseDate = "2025-01-01"
)

val fakeMovieActorsResponse = MovieActorsResponse(
    cast = listOf(
        CastItem(id = 1, name = "Actor 1", character = "Role 1"),
        CastItem(id = 2, name = "Actor 2", character = "Role 2")
    )
)

class MovieDetailsRepositoryImplTest {

    private lateinit var moviesApi: MoviesApi
    private lateinit var repository: MovieDetailsRepositoryImpl

    @Before
    fun setUp() {
        moviesApi = mockk()
        repository = MovieDetailsRepositoryImpl(moviesApi)
    }

    @Test
    fun `getMovieDetails should return movie details from API`() = runBlocking {
        // Arrange
        val movieId = 1
        coEvery { moviesApi.getMovieDetails(movieId) } returns fakeMovieDetailsResponse

        // Act
        val result = repository.getMovieDetails(movieId)

        // Assert
        assertEquals(fakeMovieDetailsResponse, result)
        coVerify(exactly = 1) { moviesApi.getMovieDetails(movieId) }
    }

    @Test
    fun `getMovieActors should return movie actors from API`() = runBlocking {
        // Arrange
        val movieId = 1
        coEvery { moviesApi.getMovieActors(movieId) } returns fakeMovieActorsResponse

        // Act
        val result = repository.getMovieActors(movieId)

        // Assert
        assertEquals(fakeMovieActorsResponse, result)
        coVerify(exactly = 1) { moviesApi.getMovieActors(movieId) }
    }
}
