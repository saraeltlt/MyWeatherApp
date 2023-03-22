package com.example.myweatherapp.datasource.network


import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.utils.Constant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiServer {

    @GET("data/2.5/onecall?")
    suspend fun getCurrentWeatherByLatAndLon(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String,
        @Query("units") units: String,
        @Query("exclude") exclude: String = "minutely",
        @Query("appid") appid: String = Constant.API_KEY
    ): Forecast
}
