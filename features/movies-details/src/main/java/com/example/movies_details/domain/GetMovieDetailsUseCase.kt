package com.example.movies_details.domain

import android.util.Log
import com.example.movies_details.data.MovieDetailsMapper
import com.example.movies_details.data.MovieDetailsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(private val movieDetailsRepository: MovieDetailsRepository) {
    fun execute(movieId: Int): Flow<MovieDetailsModel> = flow {
        val movieDetailsResponse = movieDetailsRepository.getMovieDetails(movieId)
        val movieDetails = MovieDetailsMapper.mapDtoToModel(movieDetailsResponse)
       // Log.d("movieDetails", movieDetailsResponse.backdropPath.toString())
        emit(movieDetails)
    }.flowOn(Dispatchers.IO)
}
