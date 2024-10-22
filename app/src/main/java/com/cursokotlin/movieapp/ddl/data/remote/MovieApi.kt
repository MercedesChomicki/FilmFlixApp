package com.cursokotlin.movieapp.ddl.data.remote

import com.cursokotlin.movieapp.ddl.models.MovieResponse
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): MovieResponse
}