package com.cursokotlin.movieapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cursokotlin.movieapp.BuildConfig
import com.cursokotlin.movieapp.databinding.ItemMovieBinding
import com.cursokotlin.movieapp.ddl.models.Movie

/**
 * El Adapter es responsable de inflar el layout de cada ítem (item_movie.xml) en el RecyclerView.
 */
class MovieAdapter(
    private var movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit // Callback para manejar el clic
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(private var binding : ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie, onMovieClick: (Movie) -> Unit) {
            binding.movieTitle.text = movie.title
            Glide.with(binding.root.context)
                .load("${BuildConfig.BASE_URL_IMAGES}${movie.posterPath}")
                .into(binding.moviePoster)

            // Manejo del clic en la CardView
            binding.cardViewMovie.setOnClickListener {
                onMovieClick(movie)  // Llamada al callback con la película seleccionada
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position], onMovieClick)
    }

    override fun getItemCount() = movies.size

    // Método para actualizar la lista de películas
    @SuppressLint("NotifyDataSetChanged")
    fun updateMovies(newMovies: List<Movie>){
        movies = newMovies
        notifyDataSetChanged()
    }

}