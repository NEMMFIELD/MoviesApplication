package com.example.movies_details.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailsModel(
    val id: Int?,
    val title: String?,
    val backdropPath: String?,
    val overview: String?,
    val rating: Float?,
    val genres: List<String?>?
) : Parcelable {
    val fullbackDropUrl: String?
        get() = backdropPath?.let { "https://image.tmdb.org/t/p/w500$it" }
}
