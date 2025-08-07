package com.example.movies_popular.domain

import com.example.core_model.MovieMapper
import com.example.core_model.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(private val popularRepository: PopularRepository) {
    fun execute(page: Int): Flow<List<MovieModel>?> = flow {
        val popularDtoResponse = popularRepository.loadPopularMovies(page)
        val popularMovies = popularDtoResponse.results?.map { movieDto ->
            MovieMapper.mapDtoToModel(movieDto)
        }
        emit(popularMovies)
    }.flowOn(Dispatchers.IO)
}

