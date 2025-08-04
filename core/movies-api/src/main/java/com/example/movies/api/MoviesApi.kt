package com.example.movies.api

import com.example.movies.api.model.ActorMovieCreditsResponse
import com.example.movies.api.model.MovieActorsResponse
import com.example.movies.api.model.MovieDetailsResponse
import com.example.movies.api.model.NowPlayingResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): NowPlayingResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
    ): MovieDetailsResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieActors(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): MovieActorsResponse

    @GET("person/{person_id}/movie_credits")
    suspend fun getActorMoviesCredits(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String
    ): ActorMovieCreditsResponse
}
