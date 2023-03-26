package com.example.myweatherapp.datasource

import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.notifications.notificationmodel.Alert

interface RepoInterface {
    //Retrofit (remote source)
    suspend fun getCurrentWeather(lat: Double,
                                  lon: Double,
                                  lang: String="en",
                                  units: String="metric"): Forecast



    //Room (local source)
    suspend fun getWeatherDataFromDB(): Forecast?
    suspend fun insertWeatherData(forecast: Forecast)
    suspend fun deleteCurrentWeather()

    suspend fun getAllFav(): List<Forecast>
    suspend fun insertFav(forecast: Forecast)
    suspend fun deleteFav(forecast: Forecast)

    suspend fun getAllAlerts(): List<Alert>
    suspend fun insertAlert(alert: Alert)
    suspend fun deleteAlert(alert: Alert)





}
