package com.cursokotlin.movieapp.ddl.data.dto

import com.cursokotlin.movieapp.ddl.models.Movie
import com.google.gson.annotations.SerializedName

class MovieDetailsDto (
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("poster_path")
    var posterPath: String,
    @SerializedName("backdrop_path")
    var backdropPath: String,
    @SerializedName("overview")
    var overview: String,
    @SerializedName("genres")
    var genres: List<GenreDto>,
    @SerializedName("vote_average")
    var voteAverage: Double
) {

    fun toMovie() : Movie {
        return Movie (
            id = id,
            title = title,
            posterPath = posterPath,
            backdropPath = backdropPath,
            overview = overview,
            genres = genres.map { it.toGenre() },
            voteAverage = voteAverage,
        )
    }

}

/**
 * {
 *   "backdrop_path": "/p5ozvmdgsmbWe0H8Xk7Rc8SCwAB.jpg",
 *   "genres": [
 *     {
 *       "id": 16,
 *       "name": "Animation"
 *     },
 *     {
 *       "id": 10751,
 *       "name": "Family"
 *     },
 *     {
 *       "id": 12,
 *       "name": "Adventure"
 *     },
 *     {
 *       "id": 35,
 *       "name": "Comedy"
 *     },
 *     {
 *       "id": 18,
 *       "name": "Drama"
 *     }
 *   ],
 *   "id": 1022789,
 *   "imdb_id": "tt22022452",
 *   "origin_country": [
 *     "US"
 *   ],
 *   "original_language": "en",
 *   "original_title": "Inside Out 2",
 *   "overview": "Teenager Riley's mind headquarters is undergoing a sudden demolition to make room for something entirely unexpected: new Emotions! Joy, Sadness, Anger, Fear and Disgust, who’ve long been running a successful operation by all accounts, aren’t sure how to feel when Anxiety shows up. And it looks like she’s not alone.",
 *   "popularity": 1847.374,
 *   "poster_path": "/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg",
 *   "release_date": "2024-06-11",
 *   "title": "Inside Out 2",
 *   "video": false,
 *   "vote_average": 7.616,
 *   "vote_count": 4431
 * }
 */