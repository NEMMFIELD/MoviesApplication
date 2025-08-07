package com.example.movies_upcoming.domain

import com.example.core_model.MovieMapper
import com.example.core_model.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetUpcomingMoviesUseCase @Inject constructor(private val upcomingRepository: UpcomingRepository) {
    fun execute(page: Int): Flow<List<MovieModel>?> = flow {
        val dtoResponse = upcomingRepository.getUpcomingMovies(page)
        val upcomingMovies = dtoResponse.results?.map { movie ->
            MovieMapper.mapDtoToModel(movie)
        }
        emit(upcomingMovies)
    }.flowOn(Dispatchers.IO)
}
