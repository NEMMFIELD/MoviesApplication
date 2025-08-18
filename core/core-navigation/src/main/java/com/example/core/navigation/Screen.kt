package com.example.core.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object Auth : Screen("auth")

    object NowPlaying : Screen("now_playing")
    object Popular : Screen("popular")
    object TopRated : Screen("top_rated")
    object Upcoming : Screen("upcoming")

    object MovieDetails : Screen("movie_details/{movieId}") {
        fun createRoute(movieId: Int) = "movie_details/$movieId"
    }

    object Rating : Screen("rating/{movieId}/{movieTitle}") {
        fun createRoute(movieId: Int, title: String) =
            "rating/$movieId/${Uri.encode(title)}"
    }

    object ActorMovieCredits : Screen("actor_movie_credits/{actorId}") {
        fun createRoute(actorId: Int) = "actor_movie_credits/$actorId"
    }
}
