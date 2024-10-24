package com.cursokotlin.movieapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.movieapp.ddl.data.MovieRepository
import com.cursokotlin.movieapp.ddl.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> get() = _error

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    fun getPopularMovies(apiKey: String) {
        _loading.value = true
        _error.value = false
        viewModelScope.launch(Dispatchers.IO) {
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
        }
    }
}