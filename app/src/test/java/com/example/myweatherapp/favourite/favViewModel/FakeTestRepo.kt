package com.example.myweatherapp.favourite.favViewModel

import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.model.ApiState
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.model.MyAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class FakeTestRepo ( var favWeather: MutableList<Forecast>? = mutableListOf()):RepoInterface {


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


    override suspend fun getAllAlerts(): Flow<List<MyAlert>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(myAlert: MyAlert) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(myAlert: MyAlert) {
        TODO("Not yet implemented")
    }
}