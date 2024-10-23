package com.cursokotlin.movieapp.ddl.data.remote

import android.util.Log
import com.cursokotlin.movieapp.ddl.models.Movie
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val movieApi: MovieApi
) {

    suspend fun getMovieDetails(apiKey: String, movieId: Int): Movie? {

        return try {
            val response = movieApi.getMovieDetails(movieId, apiKey)
            if (response.isSuccessful) {
                response.body()?.toMovie()
            } else {
                throw Exception("Error fetching movie details: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("MovieRemoteDataSource", "Error fetching movies: ", e)
            null
        }
    }

    suspend fun getPopularMovies(apiKey: String): List<Movie> {
        val response = movieApi.getPopularMovies(apiKey)
        if (response.isSuccessful) {
            return response.body()?.toMovies() ?: emptyList()
        } else {
            throw Exception("Error fetching movies: ${response.message()}")
        }
    }
}