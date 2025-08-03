package com.example.movies_details.data

import com.example.movies.api.model.CrewItem
import com.example.movies.api.model.MovieDetailsResponse

object MovieDetailsMapper {
    fun mapDtoToModel(detailsDto: MovieDetailsResponse?): MovieDetailsModel {
        val safeVoteAverage = detailsDto?.voteAverage

        val rating = when (safeVoteAverage) {
            is Number -> safeVoteAverage.toFloat() / 2F
            is String -> safeVoteAverage.toFloatOrNull()?.div(2F)
            else -> null
        }
        return MovieDetailsModel(
            id = detailsDto?.id,
            title = detailsDto?.title,
            backdropPath = detailsDto?.backdropPath,
            overview = detailsDto?.overview,
            rating = rating,
            genres = detailsDto?.genres?.map { it?.name }
        )
    }

    fun mapDtoActorsToModelActors(actorsDto: CrewItem?): MovieActorsModel {
        return MovieActorsModel(
            id = actorsDto?.id,
            name = actorsDto?.name,
            profilePath = actorsDto?.profilePath
        )
    }
}
