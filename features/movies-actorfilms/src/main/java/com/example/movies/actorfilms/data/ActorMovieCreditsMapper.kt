package com.example.movies.actorfilms.data

import com.example.movies.api.model.ActorCrewItem

object ActorMovieCreditsMapper {
    fun mapDtoToActorMoviesCreditsModel(responseDto: ActorCrewItem?): ActorMovieCreditsModel {
        return ActorMovieCreditsModel(id = responseDto?.id, posterPath = responseDto?.posterPath)
    }
}
