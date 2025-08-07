package com.example.movies.toprated.domain

import com.example.movies.api.model.DTOResponse

interface TopRatedRepository {
    suspend fun loadTopRatedMovies(page:Int): DTOResponse
}
