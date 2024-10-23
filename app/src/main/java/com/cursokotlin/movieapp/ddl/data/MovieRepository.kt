package com.cursokotlin.movieapp.ddl.data

import com.cursokotlin.movieapp.ddl.data.remote.MovieRemoteDataSource
import com.cursokotlin.movieapp.ddl.models.Movie
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource
) {

    suspend fun getMovieDetails(
        apiKey: String,
        movieId: Int
    ) : Movie? {
        return remoteDataSource.getMovieDetails(apiKey, movieId)
    }

    suspend fun getPopularMovies(
        apiKey: String
    ) : List<Movie> {
        return remoteDataSource.getPopularMovies(apiKey)
    }
}