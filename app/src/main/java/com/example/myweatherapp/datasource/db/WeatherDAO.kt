package com.example.myweatherapp.datasource.db

import androidx.room.*
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.notifications.notificationmodel.Alert


@Dao
interface WeatherDAO {

    @Query("select* from favorites where currentWeather LIKE:currentWeather")
    fun getWeatherDataFromDB(currentWeather:Int=1): Forecast
    @Query("DELETE  from favorites where currentWeather LIKE:currentWeather")
    fun deleteWeather(currentWeather:Int=1)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavOrCurrent(forecast: Forecast)
    @Delete
    fun deleteFav(forecast:Forecast)
    @Query("select* from favorites where currentWeather LIKE:currentWeather 1")
    fun getAllFav():List<Forecast>



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAlert(alert: Alert)
    @Delete
    fun deleteAlert(alert: Alert)
    @Query("select* from alerts")
    fun getAllAlerts(): List<Alert>
}