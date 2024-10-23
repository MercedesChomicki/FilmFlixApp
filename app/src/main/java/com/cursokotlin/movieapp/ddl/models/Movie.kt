package com.cursokotlin.movieapp.ddl.models

import com.cursokotlin.movieapp.ddl.data.dto.GenreDto

class Movie(
    val id: Int,
    val title: String,
    val posterPath: String,
    val backdropPath: String?,
    val overview: String?,
    val genres: List<Genre>?,
    val voteAverage: Double?,
)
