package com.example.movies_popular.data

import com.example.core_model.MovieModel
import com.example.movies.api.model.ResultsItem

object PopularMovieMapper {
    fun mapDtoToModel(dto: ResultsItem?): MovieModel {
        val safeVoteAverage = dto?.voteAverage

        val rating = when (safeVoteAverage) {
            is Number -> safeVoteAverage.toFloat()/2F
            is String -> safeVoteAverage.toFloatOrNull()?.div(2F)
            else -> null
        }
        return MovieModel(
            id = dto?.id,
            title = dto?.title,
            posterPath = dto?.posterPath,
            rating = rating)

    }
}
