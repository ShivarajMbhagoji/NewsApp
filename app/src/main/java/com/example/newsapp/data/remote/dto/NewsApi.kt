package com.example.newsapp.data.remote.dto

import com.example.newsapp.utils.constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything")
    suspend fun getNews(
        @Query("sources") sources: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String=API_KEY
    ):NewsResponse
}