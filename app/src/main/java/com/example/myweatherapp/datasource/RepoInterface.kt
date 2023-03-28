package com.example.myweatherapp.datasource

import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.notifications.notificationmodel.Alert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RepoInterface {
    //Retrofit (remote source)
    suspend fun getCurrentWeather(lat: Double,
                                  lon: Double,
                                  lang: String="en",
                                  units: String="metric"): StateFlow<Forecast>



    //Room (local source)
    suspend fun getWeatherDataFromDB(): Flow<Forecast>
    suspend fun insertWeatherData(forecast: Forecast)
    suspend fun deleteCurrentWeather()

    suspend fun getAllFav(): Flow<List<Forecast>>
    suspend fun insertFav(forecast: Forecast)
    suspend fun deleteFav(forecast: Forecast)

    suspend fun getAllAlerts(): Flow<List<Alert>>
    suspend fun insertAlert(alert: Alert)
    suspend fun deleteAlert(alert: Alert)





}
