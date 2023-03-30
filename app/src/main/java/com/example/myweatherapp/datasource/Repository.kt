package com.example.myweatherapp.datasource

import com.example.myweatherapp.datasource.db.LocalSource
import com.example.myweatherapp.datasource.network.RemoteSource
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.model.MyAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class Repository (
    var remoteSource: RemoteSource,
     var localSource: LocalSource,
) : RepoInterface{


    override suspend fun getCurrentWeather(lat: Double, lon: Double, lang: String, units: String): StateFlow<Forecast> {
        return remoteSource.getCurrentWeather(lat,lon,lang,units)
    }

    override suspend fun getWeatherDataFromDB(): Flow<Forecast> {
      return localSource.getWeatherDataFromDB()
    }
    override suspend fun deleteCurrentWeather(){
        localSource.deleteCurrentWeather()
    }

    override suspend fun getAllFav(): Flow<List<Forecast>> {
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



    override suspend fun getAllAlerts(): Flow<List<MyAlert>> {
      return localSource.getAllAlerts()
    }

    override suspend fun insertAlert(myAlert: MyAlert) {
       localSource.insertAlert(myAlert)
    }

    override suspend fun deleteAlert(myAlert: MyAlert) {
        localSource.deleteAlert(myAlert)
    }






}