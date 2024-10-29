package com.cursokotlin.movieapp.ddl.models

class Movie(
    val id: Int,
    val title: String,
    val posterPath: String,
    val backdropPath: String?,
    val overview: String?,
    val genres: List<Genre>?,
    val voteAverage: Double?,
)
