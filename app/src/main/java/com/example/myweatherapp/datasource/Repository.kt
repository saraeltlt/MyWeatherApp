package com.example.myweatherapp.datasource

import android.content.SharedPreferences
import com.example.myweatherapp.datasource.db.LocalSource
import com.example.myweatherapp.datasource.network.RemoteSource
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.notifications.notificationmodel.Alert

class Repository (
    var remoteSource: RemoteSource,
     var localSource: LocalSource,
    val sharedPreferences : SharedPreferences
) : RepoInterface{
    private val sharedPreferencesEditor = sharedPreferences.edit()


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

    override fun putStringInSharedPreferences(key: String, stringInput: String) {
        TODO("Not yet implemented")
    }

    override fun getStringFromSharedPreferences(key: String, stringDefault: String): String {
        TODO("Not yet implemented")
    }

    override fun putBooleanInSharedPreferences(key: String, booleanInput: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getBooleanFromSharedPreferences(key: String, booleanDefault: Boolean): Boolean {
        TODO("Not yet implemented")
    }




}