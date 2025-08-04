package com.example.movies_details.navigation

const val NOW_PLAYING_ROUTE = "now_playing"
const val MOVIE_DETAILS_ROUTE = "movie_details"
const val MOVIE_ID_ARG = "movieId"
const val ACTOR_ID_ARG = "actorId"
const val ACTOR_MOVIE_CREDITS_ROUTE = "actor_movie_credits"

fun movieDetailsRoute(movieId: Int): String = "$MOVIE_DETAILS_ROUTE/${movieId}"

fun actorMovieCreditsRoute(actorId:Int):String = "$ACTOR_MOVIE_CREDITS_ROUTE/${actorId}"
