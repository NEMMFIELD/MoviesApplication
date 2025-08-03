package com.example.movies.nowplaying.data

import com.example.movies.api.model.NowPlayingResponse
import com.example.movies.api.model.ResultsItem

object MovieMapper {
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
