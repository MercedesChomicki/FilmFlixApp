package com.cursokotlin.movieapp.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cursokotlin.movieapp.BuildConfig
import com.cursokotlin.movieapp.R
import com.cursokotlin.movieapp.databinding.ActivityDetailsMovieBinding
import com.cursokotlin.movieapp.databinding.ErrorLayoutBinding
import com.cursokotlin.movieapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MovieDetailsActivity : BaseActivity(){

    private lateinit var binding : ActivityDetailsMovieBinding
    private lateinit var errorBinding: ErrorLayoutBinding
    private val viewModel by viewModels<MovieDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        errorBinding = ErrorLayoutBinding.bind(binding.errorLayout.root)

        // Obtenemos el ID de la película del intent
        val movieId = intent.getIntExtra("movie_id", -1)
        if (movieId != -1){
            viewModel.getMovieDetails(BuildConfig.API_KEY, movieId)
        }

        observerViewModel()

        // Reintentamos cargar las películas
        errorBinding.retryButton.setOnClickListener {
            binding.errorLayout.root.visibility = View.GONE
            viewModel.getMovieDetails(BuildConfig.API_KEY, movieId)
        }
    }

    private fun observerViewModel(){

        // Observamos los cambios y actualizamos la UI
        viewModel.movie.onEach { movie ->

            // Título de la película
            binding.movieTitleDetail.text = movie.title

            // Poster de la película
            Glide.with(this)
                .load("${BuildConfig.BASE_URL_IMAGES}${movie.posterPath}")
                .apply(RequestOptions().override(Constants.IMAGE_HEIGHT_DETAILS))
                .into(binding.moviePosterDetail)

            // Sinopsis
            binding.movieSynopsisDetail.text = movie.overview

            // Rating
            binding.movieRatingDetail.text = movie.voteAverage.toString()

            // Géneros
            binding.movieGenresDetail.text = movie.genres?.joinToString(", ") { it.name }

        }.launchIn(lifecycleScope)

        viewModel.loading.onEach { loading ->
            setLoadingState(loading, binding.progressBar, binding.movieDetailsLayout)
        }.launchIn(lifecycleScope)

        viewModel.errorState.onEach { errorState ->
            if(errorState.isError) showErrorState(binding, errorState.message, binding.errorLayout, R.color.errorColor)
            else hideErrorState(binding, binding.errorLayout, R.color.backgroundColor)
        }.launchIn(lifecycleScope)
    }
}