package com.example.movies.actorfilms.domain

import com.example.movies.api.model.ActorMovieCreditsResponse

interface ActorMovieCreditsRepository {
    suspend fun getActorMoviesCredits(actorId:Int?): ActorMovieCreditsResponse
}
