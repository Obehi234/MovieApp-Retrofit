package com.example.onlinemoviestreamingservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.onlinemoviestreamingservice.api.ApiService
import com.example.onlinemoviestreamingservice.api.RetrofitInstance
import com.example.onlinemoviestreamingservice.utils.Constants.AuthorizationHeader
import kotlinx.coroutines.launch

class DetailsMovieActivity : AppCompatActivity() {
    private lateinit var imgMovie: ImageView
    private lateinit var tvMovieTitle: TextView
    private lateinit var apiService: ApiService

    private lateinit var movieBackdrop: ImageView
    private lateinit var tvMovieTagline: TextView
    private lateinit var tvMovieInfo: TextView
    private lateinit var tvReleaseDateInfo: TextView
    private lateinit var tvMovieRating: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_movie)

        apiService = RetrofitInstance.getInstance().create(ApiService::class.java)

        imgMovie = findViewById(R.id.imgMovie)
        tvMovieTitle = findViewById(R.id.tvMovieTitle)

        movieBackdrop = findViewById(R.id.imgBackground)
        tvMovieTagline = findViewById(R.id.tvMovieTagLine)
        tvMovieInfo = findViewById(R.id.tvMovieInfo)
        tvReleaseDateInfo = findViewById(R.id.tvMovieDateRelease)
        tvMovieRating = findViewById(R.id.tvMovieRating)

        val movieTitle = intent.getStringExtra("movieTitle")
        val moviePosterPath = intent.getStringExtra("moviePosterPath")
        val moviePosterUrl = intent.getStringExtra("moviePosterUrl")

        tvMovieTitle.text = movieTitle

        Glide.with(this)
            .load(moviePosterUrl)
            .into(imgMovie)

        getMovieDeets()


    }

    private fun getMovieDeets() {
        lifecycleScope.launch {
            try {
                val result = apiService.getMovies(AuthorizationHeader)
                if (result.isSuccessful) {
                    val movie = result.body()
                    if (movie != null) {
                        val backdropUrl = "https://image.tmdb.org/t/p/w780${movie.results.firstOrNull()?.backdrop_path}"
                        Glide.with(this@DetailsMovieActivity)
                            .load(backdropUrl)
                            .into(movieBackdrop)

                        tvMovieTagline.text = movie.results.firstOrNull()?.title
                        tvMovieInfo.text = movie.results.firstOrNull()?.overview
                        tvReleaseDateInfo.text = movie.results.firstOrNull()?.release_date
                        tvMovieRating.text = movie.results.firstOrNull()?.vote_average.toString()
                    }
                } else {
                    Log.d("DetailsMovieActivity", "Error: ${result.message()}")
                }
            } catch (e: Exception) {
                Log.d("DetailsMovieActivity", "Error: ${e.message}")
            }
        }
    }

}