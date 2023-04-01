package com.example.myweatherapp.datasource.db

import androidx.room.*
import com.example.myweatherapp.model.Forecast
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherDAO {

    @Query("select* from favorites where currentWeather LIKE:currentWeather")
    fun getWeatherDataFromDB(currentWeather:Int=1): Flow<Forecast>
    @Query("DELETE  from favorites where currentWeather LIKE:currentWeather")
    fun deleteWeather(currentWeather:Int=1)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavOrCurrent(forecast: Forecast)
    @Delete
    fun deleteFav(forecast:Forecast)
    @Query("select* from favorites where currentWeather LIKE:currentWeather")
    fun getAllFav(currentWeather:Int=0): Flow<List<Forecast>>




}