package com.example.myweatherapp.datasource.network

import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.utils.Constant
import kotlinx.coroutines.flow.StateFlow
import retrofit2.http.Query


interface RemoteSource {

    suspend fun getCurrentWeather( lat: Double,
                                   lon: Double,
                                   lang: String,
                                   units: String,
    ): StateFlow<Forecast>

}