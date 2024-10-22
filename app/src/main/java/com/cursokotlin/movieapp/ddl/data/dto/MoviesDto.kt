package com.cursokotlin.movieapp.ddl.data.dto

import com.cursokotlin.movieapp.ddl.models.Movie
import com.google.gson.annotations.SerializedName

class MoviesDto (
    @SerializedName("results")
    var results: List<MovieDto>
) {
    fun toMovies() : List<Movie> {
        return results.map { movieDto ->
            movieDto.toMovie()
        }
    }
}