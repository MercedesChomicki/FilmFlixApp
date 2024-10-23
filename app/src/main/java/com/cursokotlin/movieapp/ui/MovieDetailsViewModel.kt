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
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    private val _movie = MutableLiveData<Movie>()
    val movie : LiveData<Movie> get() = _movie

    fun getMovieDetails(apiKey: String, movieId: Int){
        viewModelScope.launch {
            try {
                val movieDetails = repository.getMovieDetails(apiKey, movieId)
                _movie.value = movieDetails
            } catch (e: Exception) {
                Log.e("MovieDetailsViewModel", "Error fetching movie details: $e")
            }
        }
    }
}