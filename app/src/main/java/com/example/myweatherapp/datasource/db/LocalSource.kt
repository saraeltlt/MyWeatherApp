package com.example.myweatherapp.datasource.db

import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.notifications.notificationmodel.Alert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


interface LocalSource {
    suspend fun getWeatherDataFromDB(): Flow<Forecast>
    suspend fun deleteCurrentWeather()

    //Favorites
    suspend fun getAllFav(): Flow<List<Forecast>>
    suspend fun insertFavOrCurrent(forecast: Forecast)
    suspend fun deleteFav(forecast: Forecast)

    //Alerts
    suspend fun getAllAlerts(): Flow<List<Alert>>
    suspend fun insertAlert(alert: Alert)
    suspend fun deleteAlert(alert: Alert)

}