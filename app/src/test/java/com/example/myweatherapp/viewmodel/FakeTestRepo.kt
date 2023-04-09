package com.example.myweatherapp.viewmodel

import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.model.MyAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class FakeTestRepo (): RepoInterface {

    var favWeather: MutableList<Forecast>? = mutableListOf()
    var alert: MutableList<MyAlert>? = mutableListOf()
    override suspend fun getAllFav(): Flow<List<Forecast>> = flow {

        val _weather = favWeather?.toList()
        if (_weather != null) {
            emit(_weather)
        }
    }

    override suspend fun insertFav(forecast: Forecast) {
        favWeather?.add(forecast)
    }

    override suspend fun deleteFav(forecast: Forecast) {
        favWeather?.remove(forecast)
    }



    override suspend fun getAllAlerts(): Flow<List<MyAlert>> = flow {

        val _alert = alert?.toList()
        if (_alert != null) {
            emit(_alert)
        }
    }

    override suspend fun insertAlert(myAlert: MyAlert) {
        alert?.add(myAlert)
    }

    override suspend fun deleteAlert(myAlert: MyAlert) {
       alert?.remove(myAlert)
    }



    //rest of the repo
    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String
    ): StateFlow<Forecast> {
        TODO("Not yet implemented")
    }
    override suspend fun getWeatherDataFromDB(): Flow<Forecast> {
        TODO("Not yet implemented")
    }

    override suspend fun insertWeatherData(forecast: Forecast) {
    }

    override suspend fun deleteCurrentWeather() {
        TODO("Not yet implemented")
    }
}