package com.cursokotlin.movieapp.ddl.data.dto

import com.cursokotlin.movieapp.ddl.models.Genre
import com.google.gson.annotations.SerializedName

class GenreDto (
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String
){
    fun toGenre() : Genre {
        return Genre(
            id = id,
            name = name
        )
    }
}