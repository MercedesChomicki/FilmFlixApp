package com.cursokotlin.movieapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.movieapp.R
import com.cursokotlin.movieapp.ddl.data.MovieRepository
import com.cursokotlin.movieapp.ddl.models.ErrorState
import com.cursokotlin.movieapp.ddl.models.Movie
import com.cursokotlin.movieapp.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val resourceProvider: ResourceProvider
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
                    is IOException -> ErrorState(
                        true,
                        resourceProvider.getString(R.string.error_network_title),
                        resourceProvider.getString(R.string.error_network_message)
                    )
                    is HttpException -> ErrorState(
                        true,
                        resourceProvider.getString(R.string.error_server_title),
                        e.message() ?: resourceProvider.getString(R.string.error_server_message)
                    )
                    else -> ErrorState(
                        true,
                        resourceProvider.getString(R.string.error_unexpected_title),
                        resourceProvider.getString(R.string.error_unexpected_message)
                    )
                }
            } finally {
                _loading.value = false
            }
        }
    }
}