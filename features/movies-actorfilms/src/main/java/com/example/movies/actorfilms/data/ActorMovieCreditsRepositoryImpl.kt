package com.example.movies.actorfilms.data

import com.example.movies.actorfilms.domain.ActorMovieCreditsRepository
import com.example.movies.api.MoviesApi
import com.example.movies.api.model.ActorMovieCreditsResponse
import javax.inject.Inject

class ActorMovieCreditsRepositoryImpl @Inject constructor(private val moviesApi: MoviesApi) :
    ActorMovieCreditsRepository {
    override suspend fun getActorMoviesCredits(actorId: Int?): ActorMovieCreditsResponse {
        return moviesApi.getActorMoviesCredits(actorId ?: 0, apiKey = "56b9fc3e2f7cf0c570b8d7dc71de180e")
    }
}
