package com.example.myweatherapp.datasource.network

import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.utils.Constant
import retrofit2.http.Query


interface RemoteSource {
    suspend fun getCurrentWeather( lat: Double,
                                   lon: Double,
                                   lang: String="en",
                                   units: String="metric",
                                   exclude: String = "minutely",
                                   appid: String = Constant.API_KEY
    ): Forecast

}