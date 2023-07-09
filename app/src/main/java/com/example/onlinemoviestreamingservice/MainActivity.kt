package com.example.onlinemoviestreamingservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinemoviestreamingservice.api.ApiService
import com.example.onlinemoviestreamingservice.api.RetrofitInstance
import com.example.onlinemoviestreamingservice.model.Result
import com.example.onlinemoviestreamingservice.utils.Constants.AuthorizationHeader
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiService = RetrofitInstance.getInstance().create(ApiService::class.java)

        recyclerView = findViewById(R.id.rvMovie)
        recyclerView.layoutManager = LinearLayoutManager(this)
        progressBar = findViewById(R.id.progBarMovie)

        getMovie()
    }

    private fun getMovie() {
        lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE

            val result = apiService.getMovies(AuthorizationHeader)
            if(result.isSuccessful) {
                progressBar.visibility = View.GONE
                if(result.body() != null) {
                    adapter = MovieAdapter(result.body()?.results ?: listOf(), object: MovieAdapter.OnItemClickListener{
                        override fun onItemClick(movie: Result) {
                            val bundle = Bundle()
                            bundle.putString("movieTitle", movie.title)
                            bundle.putString("moviePoster", movie.poster_path)
                            bundle.putString("moviePosterUrl", "https://image.tmdb.org/t/p/w500" + movie.poster_path)
                            bundle.putInt("movieId", movie.id)
                            val intent = Intent(this@MainActivity, DetailsMovieActivity::class.java)
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }
                    })
                    recyclerView.adapter = adapter
                    Log.d("MovieGenerator", "get Movies:${result.body()} ")
                } else {
                    Log.d("Movie Generator", "get Movies ${result.message()}")
                }
            }
        }
    }
}