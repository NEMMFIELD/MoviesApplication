package com.example.movies.api

import com.example.movies.api.model.ActorMovieCreditsResponse
import com.example.movies.api.model.MovieActorsResponse
import com.example.movies.api.model.MovieDetailsResponse
import com.example.movies.api.model.DTOResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    //Now playing movies
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): DTOResponse

    //Popular movies
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): DTOResponse

    //Top rated movies
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): DTOResponse

    //Upcoming movies
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): DTOResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US",
    ): MovieDetailsResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieActors(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): MovieActorsResponse

    @GET("person/{person_id}/movie_credits")
    suspend fun getActorMoviesCredits(
        @Path("person_id") personId: Int,
    ): ActorMovieCreditsResponse
}
