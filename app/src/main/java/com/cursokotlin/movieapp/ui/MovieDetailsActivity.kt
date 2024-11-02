package com.cursokotlin.movieapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cursokotlin.movieapp.BuildConfig
import com.cursokotlin.movieapp.databinding.ActivityDetailsMovieBinding
import com.cursokotlin.movieapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.properties.Delegates

@AndroidEntryPoint
class MovieDetailsActivity : BaseActivity(), ErrorDialogFragment.Retryable{

    private lateinit var binding : ActivityDetailsMovieBinding
    private var movieId by Delegates.notNull<Int>()
    private val viewModel by viewModels<MovieDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtenemos el ID de la película del intent
        movieId = intent.getIntExtra("movie_id", -1)
        if (movieId != -1){
            //viewModel.getMovieDetails(BuildConfig.API_KEY, movieId)
            viewModel.getMovieDetails("18964153bedioafeñ", movieId)
        }

        observerViewModel()

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
            if (errorState.isError) {
                showErrorDialog(errorState)
            }
        }.launchIn(lifecycleScope)
    }

    override fun retryConnection() {
        viewModel.getMovieDetails(BuildConfig.API_KEY, movieId)
    }
}