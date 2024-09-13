package com.avvsassignment

import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import android.widget.EditText
import android.widget.GridView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var movieApi: MovieApi
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var gridView: GridView
    private lateinit var searchView: EditText

    private var currentPage = 1
    private var currentQuery = ""
    private var totalResults = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridView = findViewById(R.id.gridView)
        searchView = findViewById(R.id.searchView)

        movieAdapter = MovieAdapter(this, listOf())
        gridView.adapter = movieAdapter


        // Create an instance of the HttpLoggingInterceptor
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Set the log level. You can choose between BASIC, HEADERS, BODY.
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Create an OkHttpClient and add the logging interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        movieApi = retrofit.create(MovieApi::class.java)

        searchView.doOnTextChanged { text, start, before, count ->

            Log.d("doOnTextChanged", "onCreate: $text")
            text?.trim()?.let { querry ->
                if (querry.length > 2) {
                    currentQuery = text.toString()
                    currentPage = 1
                    Log.d("doOnTextChanged", "insideIf: $currentQuery")
                    searchMovies(currentQuery)

                }
            }
        }

        // Setup GridView scroll listener for pagination
        gridView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {}

            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount < totalResults) {
                    currentPage++
                    searchMovies(currentQuery)
                }
            }
        })
    }

    private fun searchMovies(query: String) {
        Log.i("searchMovies", "searchMovies: query: $query, currentPage: $currentPage")
        lifecycleScope.launch {
            try {
                val response = movieApi.searchMovies("b9bd48a6", query, currentPage)
                if (response.isSuccessful && response.body() != null) {
                    val movieResponse = response.body()!!
                    totalResults = movieResponse.totalResults.toInt()

                    if (currentPage == 1) {
                        movieAdapter.setMovies(movieResponse.Search)
                    } else {
                        movieAdapter.addMovies(movieResponse.Search)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Toast.makeText(this@MainActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
