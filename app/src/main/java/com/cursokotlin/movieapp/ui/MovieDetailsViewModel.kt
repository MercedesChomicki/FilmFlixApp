package com.cursokotlin.movieapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.movieapp.ddl.data.MovieRepository
import com.cursokotlin.movieapp.ddl.models.ErrorState
import com.cursokotlin.movieapp.ddl.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _errorState = MutableStateFlow(ErrorState())
    val errorState: StateFlow<ErrorState> get() = _errorState

    private val _movie = MutableStateFlow(Movie(0, "","",null, null, 0.0))
    val movie : StateFlow<Movie> get() = _movie

    fun getMovieDetails(apiKey: String, movieId: Int){
        _loading.value = true
        _errorState.value = ErrorState() // Reset error state
        viewModelScope.launch {
            try {
                val movie = repository.getMovieDetails(apiKey, movieId)
                _movie.value = movie
            } catch (e: Exception) {
                _errorState.value = when(e) {
                    is IOException -> ErrorState(true, "Network error. Please check your connection.")
                    is HttpException ->  ErrorState(true,"Server error: \n${e.message()}")
                    else -> ErrorState(true, "Unexpected error: ${e.message}")
                }
            } finally {
                _loading.value = false
            }
        }
    }
}



/*
viewModelScope.launch {
    val result = withContext(Dispatchers.IO) {
        runCatching { repository.getMovieDetails(apiKey, movieId) }
    }
    result.onSuccess { movie ->
        if(movie!= null){
            _movie.value = movie
        }
    }.onFailure { e ->
        _errorState.value = when(e) {
            is IOException -> ErrorState(true, "Network error. Please check your connection.")
            is HttpException ->  ErrorState(true,"Server error: \n${e.message()}")
            else -> ErrorState(true, "Unexpected error: ${e.message} Popular Movies")
        }
    }
    _loading.value = false
}*/
