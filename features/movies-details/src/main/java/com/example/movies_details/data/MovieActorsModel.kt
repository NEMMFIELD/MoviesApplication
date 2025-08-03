package com.example.movies_details.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class MovieActorsModel(val id:Int?,val name:String?,val profilePath:String?): Parcelable {
    val fullProfilePath:String? get() = profilePath?.let{ "https://image.tmdb.org/t/p/w500/$it"}
}

