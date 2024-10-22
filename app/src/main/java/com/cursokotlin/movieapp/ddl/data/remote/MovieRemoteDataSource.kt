package com.cursokotlin.movieapp.ddl.data.remote

import com.cursokotlin.movieapp.ddl.models.Movie
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val movieApi: MovieApi
) {
    suspend fun getPopularMovies(apiKey: String): List<Movie> {
        val response = movieApi.getPopularMovies(apiKey)
        if (response.isSuccessful) {
            return response.body()?.toMovies() ?: emptyList()
        } else {
            throw Exception("Error fetching movies: ${response.message()}")
        }
    }
}