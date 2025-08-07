package com.example.movies_upcoming.domain

import com.example.movies.api.model.DTOResponse

interface UpcomingRepository {
   suspend fun getUpcomingMovies(page:Int): DTOResponse
}
