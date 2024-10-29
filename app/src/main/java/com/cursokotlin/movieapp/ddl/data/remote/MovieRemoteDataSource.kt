package com.cursokotlin.movieapp.ddl.data.remote

import com.cursokotlin.movieapp.ddl.models.Movie
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val movieApi: MovieApi
) {

    suspend fun getMovieDetails(apiKey: String, movieId: Int): Movie {
        val response = movieApi.getMovieDetails(movieId, apiKey)
        if (response.isSuccessful) {
            return response.body()?.toMovie() ?: throw Exception("Movie details not available") // Caso de respuesta exitosa sin datos (null):
        } else {
            throw Exception("Error fetching movie details: ${response.errorBody()?.string()}") // Caso de respuesta no exitosa (error en la API):
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