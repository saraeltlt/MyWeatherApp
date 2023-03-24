package com.example.myweatherapp.datasource.db

import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.notifications.notificationmodel.Alert


interface LocalSource {
    suspend fun getWeatherDataFromDB(): Forecast
    suspend fun deleteCurrentWeather()

    //Favorites
    suspend fun getAllFav(): List<Forecast>
    suspend fun insertFavOrCurrent(forecast: Forecast)
    suspend fun deleteFav(forecast: Forecast)

    //Alerts
    suspend fun getAllAlerts(): List<Alert>
    suspend fun insertAlert(alert: Alert)
    suspend fun deleteAlert(alert: Alert)

}