package com.example.myweatherapp.datasource.db

import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.model.MyAlert
import kotlinx.coroutines.flow.Flow


interface LocalSource {
    suspend fun getWeatherDataFromDB(): Flow<Forecast>
    suspend fun deleteCurrentWeather()

    //Favorites
    suspend fun getAllFav(): Flow<List<Forecast>>
    suspend fun insertFavOrCurrent(forecast: Forecast)
    suspend fun deleteFav(forecast: Forecast)

    //Alerts
    suspend fun getAllAlerts(): Flow<List<MyAlert>>
    suspend fun insertAlert(myAlert: MyAlert)
    suspend fun deleteAlert(myAlert: MyAlert)

}