package com.example.movies_popular.domain

import com.example.core_model.MovieModel
import com.example.movies.api.model.DTOResponse

interface PopularRepository {
   suspend fun loadPopularMovies(page:Int):DTOResponse
}
