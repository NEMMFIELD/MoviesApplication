package com.example.movies_details.domain

import com.example.movies_details.data.MovieActorsModel
import com.example.movies_details.data.MovieDetailsMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMovieActorsUseCase @Inject constructor(private val movieDetailsRepository: MovieDetailsRepository) {
    fun execute(movieId: Int): Flow<List<MovieActorsModel>?> = flow {
        val dtoResponse = movieDetailsRepository.getMovieActors(movieId)
        val actors = dtoResponse.crew?.map { crewItem ->
            MovieDetailsMapper.mapDtoActorsToModelActors(crewItem)
        }
        emit(actors)
    }.flowOn(Dispatchers.IO)
}
