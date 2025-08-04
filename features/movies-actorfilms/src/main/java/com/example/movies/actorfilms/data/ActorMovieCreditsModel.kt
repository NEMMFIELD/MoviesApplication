package com.example.movies.actorfilms.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActorMovieCreditsModel(val id: Int?, val posterPath: String?) : Parcelable {
    val fullPosterPath: String? get() = posterPath?.let { "https://image.tmdb.org/t/p/w400$it" }
}
