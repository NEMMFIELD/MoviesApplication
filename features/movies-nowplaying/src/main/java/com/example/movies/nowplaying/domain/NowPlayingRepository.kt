package com.example.movies.nowplaying.domain

import com.example.core_model.MovieModel

interface NowPlayingRepository {
   suspend fun getNowPlayingMovies(page:Int):List<MovieModel>?
}
