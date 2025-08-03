package com.example.movies.nowplaying.domain

import com.example.movies.nowplaying.data.MovieModel

interface NowPlayingRepository {
   suspend fun getNowPlayingMovies():List<MovieModel>?
}
