package com.cursokotlin.movieapp.ui

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        // Inicializamos el Adapter con un callback para manejar el click en la pelicula
        movieAdapter = MovieAdapter(emptyList()) { movie ->
            val intent = Intent(this, MovieDetailsActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            startActivity(intent)
        }

        // Configuramos el RecyclerView
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@MovieActivity, 3)
            adapter = movieAdapter
        }

        // Observamos los datos de las películas desde el ViewModel
        viewModel.movies.onEach { movies ->
            // Ocultamos el error y mostramos la lista de películas
            binding.errorLayout.visibility = View.GONE

            // Cuando los datos cambian, actualizamos el adaptador
            movieAdapter.updateMovies(movies)
        }.launchIn(lifecycleScope)

        viewModel.loading.onEach { loading ->
            if(loading) {
                binding.recyclerView.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE
            }
        }.launchIn(lifecycleScope)

        // Observa el estado de error y mensaje
        viewModel.errorState.onEach { errorState ->
            if(errorState.isError) {
                updateBackgroundColor(R.color.errorColor)
                binding.errorLayout.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.errorMessage.text = errorState.message
            } else {
                // Resetea el fondo y oculta la vista de error si no hay error
                updateBackgroundColor(R.color.backgroundColor)
                binding.errorLayout.visibility = View.GONE
            }
        }.launchIn(lifecycleScope)

        /*// Observamos el estado de error
        viewModel.error.onEach { error ->
            if(error) {
                val color = ContextCompat.getColor(this, R.color.errorColor)
                binding.mainLayout.background = ColorDrawable(color)
                binding.errorLayout.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }.launchIn(lifecycleScope)

        // Mostramos un mensaje de error según su proveniencia
        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                binding.errorMessage.text = message
            }
        }*/

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
}