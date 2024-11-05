package com.cursokotlin.movieapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.cursokotlin.movieapp.BuildConfig
import com.cursokotlin.movieapp.R
import com.cursokotlin.movieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MovieActivity : BaseActivity(), ErrorDialogFragment.Retryable {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieAdapter: MovieAdapter
    private val viewModel by viewModels<MovieViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Encuentra el campo de búsqueda y ajusta el padding
        val searchIcon = binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setImageResource(R.drawable.ic_search)
        searchIcon.adjustViewBounds = true

        initializeAdapter()
        setupRecyclerView()
        observeViewModel()
        searchMovie()

        // Cargamos las peliculas por primera vez
        viewModel.getPopularMovies(BuildConfig.API_KEY)
        //viewModel.getPopularMovies("18964153bedioafeñ") // Para testear error de conexion con la API
    }

    private fun searchMovie(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.filterMovies(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterMovies(newText.orEmpty())
                return true
            }
        })
    }

    private fun observeViewModel() {
        // Observamos los datos de las películas desde el ViewModel
        viewModel.movies.onEach { movies ->
            movieAdapter.updateMovies(movies) // Cuando los datos cambian, actualizamos el adaptador
        }.launchIn(lifecycleScope)

        viewModel.loading.onEach { loading ->
            setLoadingState(loading, binding.progressBar, binding.recyclerView)
        }.launchIn(lifecycleScope)

        viewModel.errorState.onEach { errorState ->
            if (errorState.isError) {
                showErrorDialog(errorState)
            }
        }.launchIn(lifecycleScope)
    }

    override fun retryConnection() {
        viewModel.getPopularMovies(BuildConfig.API_KEY)
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