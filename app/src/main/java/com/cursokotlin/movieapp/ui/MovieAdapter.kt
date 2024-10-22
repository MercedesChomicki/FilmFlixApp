package com.cursokotlin.movieapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cursokotlin.movieapp.BuildConfig
import com.cursokotlin.movieapp.R
import com.cursokotlin.movieapp.ddl.models.Movie

class MovieAdapter(
    private val movies: List<Movie>
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.movieTitle)
        private val poster = itemView.findViewById<ImageView>(R.id.moviePoster)

        fun bind(movie: Movie) {
            title.text = movie.title
            Glide.with(itemView.context)
                .load(BuildConfig.BASE_URL_IMAGES + movie.poster_path)
                .into(poster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size
}