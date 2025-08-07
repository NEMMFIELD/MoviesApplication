package com.example.movies.toprated.domain

import com.example.core_model.MovieMapper
import com.example.core_model.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetTopRatedMoviesUseCase @Inject constructor(private val topRatedRepository: TopRatedRepository) {
    fun execute(page: Int): Flow<List<MovieModel>?> = flow {
        val dtoResponse = topRatedRepository.loadTopRatedMovies(page)
        val topRatedMovies = dtoResponse.results?.map { movies ->
            MovieMapper.mapDtoToModel(movies)
        }
        emit(topRatedMovies)
    }.flowOn(Dispatchers.IO)
}
