package com.example.onlinemoviestreamingservice.api

import com.example.onlinemoviestreamingservice.utils.Constants.BASEURL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    fun getInstance(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASEURL)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}