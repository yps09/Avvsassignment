package com.avvsassignment

import Movie
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class MovieAdapter(private val context: Context, private var movieList: List<Movie>) : BaseAdapter() {

    override fun getCount(): Int {
        return movieList.size
    }

    override fun getItem(position: Int): Any {
        return movieList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)

        val movie = movieList[position]
        val posterImageView = view.findViewById<ImageView>(R.id.moviePoster)
        val titleTextView = view.findViewById<TextView>(R.id.movieTitle)
        val titleYear = view.findViewById<TextView>(R.id.moviewYear)

        // Load image using Glide
        Glide.with(context)
            .load(movie.Poster)
            .placeholder(R.drawable.ic_launcher_background)
            .into(posterImageView)

        titleTextView.text = movie.Title
       titleYear.text = movie.Year

        return view
    }

    fun addMovies(newMovies: List<Movie>) {
        movieList = movieList + newMovies
        notifyDataSetChanged()
    }

    fun setMovies(newMovies: List<Movie>) {
        movieList = newMovies
        notifyDataSetChanged()
    }
}
