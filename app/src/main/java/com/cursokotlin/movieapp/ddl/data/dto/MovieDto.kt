package com.cursokotlin.movieapp.ddl.data.dto

import com.cursokotlin.movieapp.ddl.models.Movie
import com.google.gson.annotations.SerializedName

class MovieDto (
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("poster_path")
    var posterPath: String
) {

    fun toMovie() : Movie {
        return Movie (
            id = id,
            title = title,
            posterPath = posterPath,
            backdropPath = null,
            overview = null,
            genres = null,
            voteAverage = null,
        )
    }
}