package com.example.movies.nowplaying.data

import com.example.core_model.MovieMapper
import com.example.core_model.MovieModel
import com.example.movies.api.MoviesApi
import com.example.movies.nowplaying.domain.NowPlayingRepository
import javax.inject.Inject

class NowPlayingRepositoryImpl @Inject constructor(private val moviesApi: MoviesApi): NowPlayingRepository {
    override suspend fun getNowPlayingMovies(page:Int): List<MovieModel>? {
        val apiResponse = moviesApi.getNowPlayingMovies("56b9fc3e2f7cf0c570b8d7dc71de180e", page = page)
        val nowPlayingMovies = apiResponse.results?.map { moviesDto ->
            MovieMapper.mapDtoToModel(moviesDto)
        }
        return nowPlayingMovies
    }
}
