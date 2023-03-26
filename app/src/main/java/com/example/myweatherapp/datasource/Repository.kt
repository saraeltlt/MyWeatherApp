package com.example.myweatherapp.datasource

import android.content.SharedPreferences
import com.example.myweatherapp.datasource.db.LocalSource
import com.example.myweatherapp.datasource.network.RemoteSource
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.notifications.notificationmodel.Alert

class Repository (
    var remoteSource: RemoteSource,
     var localSource: LocalSource,
) : RepoInterface{


    override suspend fun getCurrentWeather(lat: Double, lon: Double, lang: String, units: String): Forecast {
        return remoteSource.getCurrentWeather(lat,lon,lang,units)
    }

    override suspend fun getWeatherDataFromDB(): Forecast? {
      return localSource.getWeatherDataFromDB()
    }
    override suspend fun deleteCurrentWeather(){
        localSource.deleteCurrentWeather()
    }

    override suspend fun getAllFav(): List<Forecast> {
        return localSource.getAllFav()
    }

    override suspend fun insertFav(forecast: Forecast) {
        forecast.currentWeather=0
       localSource.insertFavOrCurrent(forecast)
    }

    override suspend fun deleteFav(forecast: Forecast) {
        localSource.deleteFav(forecast)
    }

    override suspend fun insertWeatherData(forecast: Forecast) {
        deleteCurrentWeather()
        forecast.currentWeather=1
        localSource.insertFavOrCurrent(forecast)
    }



    override suspend fun getAllAlerts(): List<Alert> {
      return localSource.getAllAlerts()
    }

    override suspend fun insertAlert(alert: Alert) {
       localSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        localSource.deleteAlert(alert)
    }






}