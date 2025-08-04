package com.example.movies.actorfilms.domain

import com.example.movies.actorfilms.data.ActorMovieCreditsMapper
import com.example.movies.actorfilms.data.ActorMovieCreditsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetActorMovieCreditsUseCase @Inject constructor(private val actorMoviesCreditsRepository: ActorMovieCreditsRepository) {
    fun execute(actorId: Int?): Flow<List<ActorMovieCreditsModel>?> = flow {
        val dtoResponse = actorMoviesCreditsRepository.getActorMoviesCredits(actorId)
        val actorMovieCredits = dtoResponse.crew?.map { crewItem ->
            ActorMovieCreditsMapper.mapDtoToActorMoviesCreditsModel(crewItem)
        }
        emit(actorMovieCredits)
    }.flowOn(Dispatchers.IO)
}
