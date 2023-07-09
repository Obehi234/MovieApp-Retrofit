package com.example.onlinemoviestreamingservice.api

import com.example.onlinemoviestreamingservice.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {

    @GET("movie/popular")
    suspend fun getMovies(
        @Header("Authorization") authorization: String
    ): Response<MovieResponse>



}