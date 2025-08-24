package com.example.movies_details.navigation

import android.net.Uri
const val AUTH_ROUTE = "auth"
const val NOW_PLAYING_ROUTE = "now_playing"
const val RATING_ROUTE = "rate_movie"
const val MOVIE_DETAILS_ROUTE = "movie_details"
const val ACTOR_MOVIE_CREDITS_ROUTE = "actor_movie_credits"

fun movieDetailsRoute(movieId: Int): String = "$MOVIE_DETAILS_ROUTE/${movieId}"
fun actorMovieCreditsRoute(actorId:Int):String = "$ACTOR_MOVIE_CREDITS_ROUTE/${actorId}"
fun rateMovieRoute(movieId: Int, movieTitle: String) =
    "$RATING_ROUTE/$movieId/${Uri.encode(movieTitle)}"
