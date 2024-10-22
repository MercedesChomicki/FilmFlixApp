package com.cursokotlin.movieapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.movieapp.BuildConfig
import com.cursokotlin.movieapp.R
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

        // Setup RecyclerView
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        viewModel.movies.observe(this) { movies ->
            movieAdapter = MovieAdapter(movies)
            recyclerView.adapter = movieAdapter
        }

        viewModel.getPopularMovies(BuildConfig.API_KEY)
    }


}