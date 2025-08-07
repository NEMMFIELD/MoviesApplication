package com.example.core_model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieModel(
    val id: Int?,
    val title: String?,
    val posterPath: String?,
    val rating: Float?,
    val releaseDate:String? = null
) : Parcelable {
    val fullPosterUrl: String?
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
}
