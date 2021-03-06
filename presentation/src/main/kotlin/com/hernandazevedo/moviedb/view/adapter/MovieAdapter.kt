package com.hernandazevedo.moviedb.view.adapter

import android.app.Activity
import android.content.Context
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.hernandazevedo.moviedb.Movie
import com.hernandazevedo.moviedb.R

class MovieAdapter(private var movies: MutableList<Movie>? = null,
                   private val context: Context,
                   private val itemClick: (movie: Movie,
                                           options: ActivityOptionsCompat
                   ) -> Unit?) :
    RecyclerView.Adapter<MovieAdapter.MoviesViewHolder>() {

    val favItems: MutableMap<String, Boolean> = HashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_list, parent, false)
        return MoviesViewHolder(view)
    }

    fun setMovies(movies: List<Movie>?) {
        this.movies = movies as MutableList<Movie>?
        refreshFavMap(movies)
        notifyDataSetChanged()
    }

    private fun refreshFavMap(newItems: List<Movie>?) {
        newItems?.map {
            it.imdbID.let { it1 ->
                it.favored.let { it2 ->
                    favItems.put(it1, it2)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        movies?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = movies?.size ?: 0

    inner class MoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title by lazy { view.findViewById<TextView>(R.id.movieTitle) }
        val year by lazy { view.findViewById<TextView>(R.id.movieReleaseYear) }
        val image by lazy { view.findViewById<ImageView>(R.id.movieImageView) }
        val favoriteContainer by lazy { view.findViewById<View>(R.id.favoriteContainer) }
        val voteContainer by lazy { view.findViewById<View>(R.id.voteContainer) }

        fun bind(movie: Movie) {
            populateMovieData(movie)

            //even odd
            if (layoutPosition % 2 == 1) {
                favoriteContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            } else {
                favoriteContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
            }

            itemView.setOnClickListener {
                itemClick(movie, animationTransitionSetup())
            }
        }

        private fun populateMovieData(movie: Movie) {
            val options = RequestOptions()
                .priority(Priority.HIGH)

            Glide.with(context)
                .load(movie.posterUrl)
                .apply(options)
                .into(image)

            title.text = movie.title
            year.text = movie.year
        }

        private fun animationTransitionSetup(): ActivityOptionsCompat {
            val p1 = Pair(title as View,
                context.getString(R.string.title_transition))
            val p2 = Pair(year as View,
                context.getString(R.string.year_transition))
            val p3 = Pair(voteContainer as View,
                context.getString(R.string.vote_transition))
            val p4 = Pair(favoriteContainer as View,
                context.getString(R.string.fav_container_transition))
            val options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(context as Activity, p1, p2, p3, p4)
            return options
        }
    }
}