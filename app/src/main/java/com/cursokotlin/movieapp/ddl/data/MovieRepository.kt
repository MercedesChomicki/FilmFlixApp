package com.cursokotlin.movieapp.ddl.data

import com.cursokotlin.movieapp.ddl.data.remote.MovieApi
import com.cursokotlin.movieapp.ddl.data.remote.MovieRemoteDataSource
import com.cursokotlin.movieapp.ddl.models.Movie
import javax.inject.Inject

class MovieRepository @Inject constructor(
    //private val remoteDataSource: MovieRemoteDataSource
    private val movieApi: MovieApi
) {

    suspend fun getPopularMovies(apiKey: String) = movieApi.getPopularMovies(apiKey)

    /*suspend fun getPopularMovies(
        apiKey: String
    ) : List<Movie>? {
        return remoteDataSource.getPopularMovies(apiKey)
    }*/

    /*suspend fun getPopularMovies(
        apiKey: String
    ) : List<Movie>? {
        //return remoteDataSource.getPopularMovies(apiKey)
        return movieApi.getPopularMovies(apiKey)
    }*/
}