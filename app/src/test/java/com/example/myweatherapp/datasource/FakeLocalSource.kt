package com.example.myweatherapp.datasource

import com.example.myweatherapp.datasource.db.LocalSource
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.model.MyAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalSource( var favWeather: MutableList<Forecast> =  mutableListOf()): LocalSource {

    override suspend fun getAllFav(): Flow<List<Forecast>> = flow {
        val _weather = favWeather.toList()
        emit(_weather)
    }


    override suspend fun insertFavOrCurrent(forecast: Forecast) {
        favWeather.add(forecast)
    }

    override suspend fun deleteFav(forecast: Forecast) {
        favWeather.remove(forecast)
    }




    override suspend fun getWeatherDataFromDB(): Flow<Forecast> {
        TODO("Not yet implemented")
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