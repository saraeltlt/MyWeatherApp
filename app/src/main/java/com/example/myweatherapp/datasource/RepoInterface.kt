package com.example.myweatherapp.datasource

import com.example.myweatherapp.model.Forecast

interface RepoInterface {
    suspend fun getCurrentWeather(lat: Double,
                                  lon: Double,
                                  lang: String="en",
                                  units: String="metric"): Forecast

}
