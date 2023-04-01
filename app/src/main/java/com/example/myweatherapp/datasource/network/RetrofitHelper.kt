package com.example.myweatherapp.datasource.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    const val baseUrl = "https://api.openweathermap.org/"

        val retrofit= Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}

