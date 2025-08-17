package com.example.movies.api

import com.example.movies.api.model.ActorMovieCreditsResponse
import com.example.movies.api.model.CreateSessionRequest
import com.example.movies.api.model.CreateSessionResponse
import com.example.movies.api.model.DTOResponse
import com.example.movies.api.model.MovieActorsResponse
import com.example.movies.api.model.MovieDetailsResponse
import com.example.movies.api.model.RatingRequest
import com.example.movies.api.model.RatingResponse
import com.example.movies.api.model.RatingStatus
import com.example.movies.api.model.RequestTokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("authentication/token/new")
    suspend fun getRequestToken(
    ): RequestTokenResponse

    @POST("authentication/session/new")
    suspend fun createSession(
        @Body request: CreateSessionRequest
    ): CreateSessionResponse

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

    @POST("movie/{movie_id}/rating")
    suspend fun addRating(
        @Path("movie_id") movieId: Int,
        @Query("session_id") sessionId: String,
        @Body rating: RatingRequest
    ): RatingResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieActors(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): MovieActorsResponse

    @GET("person/{person_id}/movie_credits")
    suspend fun getActorMoviesCredits(
        @Path("person_id") personId: Int,
    ): ActorMovieCreditsResponse

    //Check if movies is rated
    @GET("movie/{movie_id}/account_states")
    suspend fun getRatingStatus(
        @Path("movie_id") movieId: Int?,
        @Query("session_id") sessionId: String?,
    ): RatingStatus

}
