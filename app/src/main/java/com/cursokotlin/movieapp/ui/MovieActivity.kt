package com.cursokotlin.movieapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.cursokotlin.movieapp.BuildConfig
import com.cursokotlin.movieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

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
        viewModel.movies.observe(this) { movies ->
            if(movies != null){
                // Ocultamos el error y mostramos la lista de películas
                binding.errorLayout.visibility = View.GONE

                // Cuando los datos cambian, actualizamos el adaptador
                movieAdapter.updateMovies(movies)
            }
        }

        viewModel.loading.observe(this) { loading ->
            if(loading) {
                binding.recyclerView.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE
            }
        }

        // Observamos el estado de error
        viewModel.error.observe(this) { error ->
            if(error) {
                binding.errorLayout.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }

        // Mostramos un mensaje de error según su proveniencia
        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                binding.errorMessage.text = message
            }
        }

        // Reintentamos cargar las películas
        binding.retryButton.setOnClickListener {
            viewModel.getPopularMovies(BuildConfig.API_KEY)
        }

        // Cargamos las peliculas por primera vez
        viewModel.getPopularMovies(BuildConfig.API_KEY)
        //viewModel.getPopularMovies("18964153bedioafeñ")
    }
}