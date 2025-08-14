package com.example.movies.actorfilms.data

import com.example.movies.actorfilms.domain.ActorMovieCreditsRepository
import com.example.movies.api.MoviesApi
import com.example.movies.api.model.ActorMovieCreditsResponse
import javax.inject.Inject

class ActorMovieCreditsRepositoryImpl @Inject constructor(private val moviesApi: MoviesApi) :
    ActorMovieCreditsRepository {
    override suspend fun getActorMoviesCredits(actorId: Int?): ActorMovieCreditsResponse {
        return moviesApi.getActorMoviesCredits(actorId ?: 0)
    }
}
