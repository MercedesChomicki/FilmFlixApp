package com.cursokotlin.movieapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.movieapp.R
import com.cursokotlin.movieapp.ddl.data.MovieRepository
import com.cursokotlin.movieapp.ddl.models.ErrorState
import com.cursokotlin.movieapp.ddl.models.Movie
import com.cursokotlin.movieapp.utils.ResourceProvider
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
    private val repository: MovieRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _errorState = MutableStateFlow(ErrorState())
    val errorState: StateFlow<ErrorState> get() = _errorState

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> get() = _movies

    private val allMovies = mutableListOf<Movie>() // Lista completa de películas

    private val _noResults = MutableStateFlow(false)
    val noResults : StateFlow<Boolean> get() = _noResults

    private var hasSearched = false // Estado para saber si hubo una búsqueda

    fun getPopularMovies(apiKey: String) {
        _loading.value = true
        _errorState.value = ErrorState() // Resetear el errorState
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { repository.getPopularMovies(apiKey) }
            }
            result.onSuccess { movies ->
                allMovies.clear()
                allMovies.addAll(movies)
                _movies.value = movies
            }.onFailure { e ->
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
            }
            _loading.value = false
        }
    }

    fun filterMovies(query: String){
        hasSearched = query.isNotEmpty() // Marcar si se ha hecho una búsqueda
        val filteredMovies = if (query.isEmpty()) {
            allMovies
        } else {
            allMovies.filter { it.title.contains(query, ignoreCase = true) }
        }
        _movies.value = filteredMovies
        _noResults.value = hasSearched && filteredMovies.isEmpty() // Emitir si no hay resultados
    }
}