package com.cursokotlin.movieapp.ddl.data.remote

import com.cursokotlin.movieapp.ddl.models.Movie
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val movieApi: MovieApi
) {

    //suspend fun getPopularMovies(apiKey: String) = movieApi.getPopularMovies(apiKey)

    /*suspend fun getPopularMovies(apiKey: String): List<Movie> {
        val response = movieApi.getPopularMovies(apiKey)
        if (response.isSuccessful) {
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error fetching movies: ${response.message()}")
        }
    }*/

    /*suspend fun getPopularMovies(
        apiKey: String
    ): List<Movie>? {
        return withContext(Dispatchers.IO){
            try {
                val moviesDto = movieApi.getPopularMovies(apiKey)
                val movies = moviesDto.body()?.toMovie()
                return@withContext movies
            } catch (e: Exception){
                e.printStackTrace()
                return@withContext null
            }
        }
    }*/
}