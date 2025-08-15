package com.example.movies_details.navigation

import android.net.Uri
const val NOW_PLAYING_ROUTE = "now_playing"
const val POPULAR_ROUTE = "popular"
const val TOP_RATED_ROUTE = "top_rated"
const val UPCOMING_ROUTE = "upcoming"
const val RATING_ROUTE = "rate_movie"
const val MOVIE_DETAILS_ROUTE = "movie_details"
const val MOVIE_ID_ARG = "movieId"
const val MOVIE_TITLE_ARG = "title"
const val ACTOR_ID_ARG = "actorId"
const val ACTOR_MOVIE_CREDITS_ROUTE = "actor_movie_credits"

fun movieDetailsRoute(movieId: Int): String = "$MOVIE_DETAILS_ROUTE/${movieId}"
fun actorMovieCreditsRoute(actorId:Int):String = "$ACTOR_MOVIE_CREDITS_ROUTE/${actorId}"
fun rateMovieRoute(movieId: Int, movieTitle: String) =
    "$RATING_ROUTE/$movieId/${Uri.encode(movieTitle)}"
