package com.akhil.newssearchapp.services

import com.akhil.newssearchapp.modals.NewsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//api end points
interface ApiService {

    @GET("everything")
    suspend fun getNews(@Query("q") query: String,@Query("language") language: String, @Query("apiKey") apiKey: String): Response<NewsApiResponse>
    @GET("everything")
    suspend fun getNewsDate(@Query("q") query: String,@Query("language") language: String,@Query("from") from: String,@Query("to") to: String, @Query("apiKey") apiKey: String): Response<NewsApiResponse>

}