package com.example.movies_details.navigation

const val NOW_PLAYING_ROUTE = "now_playing"
const val MOVIE_DETAILS_ROUTE = "movie_details"
const val MOVIE_ID_ARG = "movieId"

fun movieDetailsRoute(movieId: Int): String = "$MOVIE_DETAILS_ROUTE/${movieId}"
