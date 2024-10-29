package com.cursokotlin.movieapp.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cursokotlin.movieapp.BuildConfig
import com.cursokotlin.movieapp.R
import com.cursokotlin.movieapp.databinding.ActivityDetailsMovieBinding
import com.cursokotlin.movieapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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

        observerViewModel()

        // Reintentamos cargar las películas
        binding.retryButton.setOnClickListener {
            binding.errorLayout.visibility = View.GONE
            viewModel.getMovieDetails(BuildConfig.API_KEY, movieId)
        }
    }

    private fun observerViewModel(){

        viewModel.loading.onEach { loading ->
            setLoadingState(loading)
        }.launchIn(lifecycleScope)

        viewModel.errorState.onEach { errorState ->
            if(errorState.isError) showErrorState(errorState.message)
            else hideErrorState()
        }.launchIn(lifecycleScope)

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
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.movieDetailsLayout.isVisible = !isLoading
        binding.progressBar.isVisible = isLoading
    }

    private fun updateBackgroundColor(colorResId: Int) {
        val color = ContextCompat.getColor(this, colorResId)
        binding.movieDetailsLayout.background = ColorDrawable(color)
    }

    private fun showErrorState(message: String) {
        updateBackgroundColor(R.color.errorColor)
        binding.errorLayout.visibility = View.VISIBLE
        binding.movieDetailsLayout.visibility = View.GONE
        binding.errorMessage.text = message
    }

    private fun hideErrorState() {
        // Resetea el fondo y oculta la vista de error si no hay error
        updateBackgroundColor(R.color.backgroundColor)
        binding.errorLayout.visibility = View.GONE
    }
}