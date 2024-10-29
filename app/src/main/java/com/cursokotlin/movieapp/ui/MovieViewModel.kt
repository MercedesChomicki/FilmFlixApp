package com.cursokotlin.movieapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.movieapp.ddl.data.MovieRepository
import com.cursokotlin.movieapp.ddl.models.ErrorState
import com.cursokotlin.movieapp.ddl.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _errorState = MutableStateFlow(ErrorState())
    val errorState: StateFlow<ErrorState> get() = _errorState

    /*private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> get() = _error

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage*/

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> get() = _movies

    /*private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies*/

    fun getPopularMovies(apiKey: String) {
        _loading.value = true
        _errorState.value = ErrorState() // Reset error state
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { repository.getPopularMovies(apiKey) }
            }
            result.onSuccess { movies ->
                _movies.value = movies
            }.onFailure { e ->
                _errorState.value = when(e) {
                    is IOException -> ErrorState(true, "Network error. Please check your connection.")
                    is HttpException ->  ErrorState(true,"Server error: \n${e.message()}")
                    else -> ErrorState(true, "Unexpected error: ${e.message} Popular Movies")
                }
            }
            _loading.value = false
        }

        /*viewModelScope.launch(Dispatchers.IO) {
            try {
                val movieList = repository.getPopularMovies(apiKey)
                withContext(Dispatchers.Main) {
                    _movies.value = movieList
                }
            }  catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    _error.value = true
                    _errorMessage.value = "Network error. Please check your connection."
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    _error.value = true
                    _errorMessage.value = "Server error: \n${e.message()}"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = true
                    _errorMessage.value = "Unexpected error: ${e.message} Popular Movies"
                }
            }  finally {
                withContext(Dispatchers.Main) {
                    _loading.value = false
                }
            }
        }*/
    }
}