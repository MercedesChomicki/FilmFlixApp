package com.cursokotlin.movieapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.movieapp.ddl.data.MovieRepository
import com.cursokotlin.movieapp.ddl.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    fun getPopularMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies(apiKey)
                _movies.value = response
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error fetching movies: $e")
            }
        }
    }
}