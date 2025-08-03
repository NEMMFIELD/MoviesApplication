package com.example.movies.nowplaying.domain

import com.example.movies.nowplaying.data.MovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NowPlayingMoviesUseCase @Inject constructor (private val repository: NowPlayingRepository) {
    fun execute(): Flow<List<MovieModel>?> = flow {
        emit(repository.getNowPlayingMovies())
    }.flowOn(Dispatchers.IO)
}
