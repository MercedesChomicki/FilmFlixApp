package com.cursokotlin.movieapp.ddl.data.dto

import com.cursokotlin.movieapp.ddl.models.Movie
import com.google.gson.annotations.SerializedName

class MovieDto (
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("poster_path")
    var poster_path: String
) {

    fun toMovie() : Movie {
        return Movie (
            id = id,
            title = title,
            poster_path = poster_path
        )
    }
}