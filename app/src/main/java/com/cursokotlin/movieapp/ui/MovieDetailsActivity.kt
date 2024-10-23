package com.cursokotlin.movieapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cursokotlin.movieapp.BuildConfig
import com.cursokotlin.movieapp.databinding.ActivityDetailsMovieBinding
import com.cursokotlin.movieapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailsMovieBinding
    private val viewModel by viewModels<MovieDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtenemos el ID de la película del intent
        val movieId = intent.getIntExtra("movie_id", -1)
        if (movieId != -1){
            viewModel.getMovieDetails(BuildConfig.API_KEY, movieId)
        }

        // Observamos los cambios y actualizamos la UI
        viewModel.movie.observe(this) { movie ->
            // Título de la película
            binding.movieTitleDetail.text = movie.title

            // Poster de la película
            Glide.with(this)
                .load("${BuildConfig.BASE_URL_IMAGES}${movie.posterPath}")
                .apply(RequestOptions().override(Constants.IMAGE_HEIGHT_DETAILS))
                .into(binding.moviePosterDetail)

            /*// Imagen de fondo de la película
            Glide.with(this)
                .load("${BuildConfig.BASE_URL_IMAGES}${movie.backdropPath}")
                .apply(RequestOptions().override(Constants.IMAGE_BACKDROP_HEIGHT)) // Puedes ajustar el tamaño si lo deseas
                .into(binding.movieBackdropDetail)*/

            // Sinopsis
            binding.movieSynopsisDetail.text = movie.overview

            // Rating
            binding.movieRatingDetail.text = movie.voteAverage.toString()

            // Géneros
            binding.movieGenresDetail.text = movie.genres?.joinToString(", ") { it.name }
        }
    }
}