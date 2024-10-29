package com.cursokotlin.movieapp.ui

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.cursokotlin.movieapp.BuildConfig
import com.cursokotlin.movieapp.R
import com.cursokotlin.movieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MovieActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var movieAdapter: MovieAdapter
    private val viewModel by viewModels<MovieViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeAdapter()
        setupRecyclerView()
        observeViewModel()

        // Reintentamos cargar las películas
        binding.retryButton.setOnClickListener {
            binding.errorLayout.visibility = View.GONE
            viewModel.getPopularMovies(BuildConfig.API_KEY)
        }

        // Cargamos las peliculas por primera vez
        //viewModel.getPopularMovies(BuildConfig.API_KEY)
        viewModel.getPopularMovies("18964153bedioafeñ")
    }

    private fun updateBackgroundColor(colorResId: Int) {
        val color = ContextCompat.getColor(this, colorResId)
        binding.mainLayout.background = ColorDrawable(color)
    }

    private fun showErrorState(message: String) {
        updateBackgroundColor(R.color.errorColor)
        binding.errorLayout.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.errorMessage.text = message
    }

    private fun hideErrorState() {
        // Resetea el fondo y oculta la vista de error si no hay error
        updateBackgroundColor(R.color.backgroundColor)
        binding.errorLayout.visibility = View.GONE
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.recyclerView.isVisible = !isLoading
        binding.progressBar.isVisible = isLoading
    }

    private fun observeViewModel(){

        // Observamos los datos de las películas desde el ViewModel
        viewModel.movies.onEach { movies ->
            hideErrorState() // Ocultamos el error y mostramos la lista de películas
            movieAdapter.updateMovies(movies) // Cuando los datos cambian, actualizamos el adaptador
        }.launchIn(lifecycleScope)

        viewModel.loading.onEach { loading ->
            setLoadingState(loading)
        }.launchIn(lifecycleScope)

        viewModel.errorState.onEach { errorState ->
            if(errorState.isError) showErrorState(errorState.message)
            else hideErrorState()
        }.launchIn(lifecycleScope)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@MovieActivity, 3)
            adapter = movieAdapter
        }
    }

    private fun initializeAdapter() {
        // Inicializamos el Adapter con un callback para manejar el click en la pelicula
        movieAdapter = MovieAdapter(emptyList()) { movie ->
            val intent = Intent(this, MovieDetailsActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            startActivity(intent)
        }
    }
}