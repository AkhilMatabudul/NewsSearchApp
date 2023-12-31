package com.akhil.newssearchapp.utils

import com.akhil.newssearchapp.services.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Api base url
    const val BASE_URL = "https://newsapi.org/v2/"

    // Api key
    const val apiKey = "710119f4520a4c25b4ab12e46322e7db"

    // RetroFit object / fetch data from API
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}