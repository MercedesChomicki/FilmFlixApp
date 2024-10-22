package com.cursokotlin.movieapp.ddl.models

import com.cursokotlin.movieapp.ddl.data.dto.MovieDto

data class Movie(
    val id: Int,
    val title: String,
    val poster_path: String
)

data class MovieResponse(
    val results: List<Movie>
)
