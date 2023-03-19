package com.example.myweatherapp.datasource

import com.example.myweatherapp.datasource.db.LocalSource
import com.example.myweatherapp.datasource.network.RemoteSource
import com.example.myweatherapp.model.Forecast

class Repository private constructor(
    var remoteSource: RemoteSource,
     var localSource: LocalSource
) : RepoInterface{

    companion object{
        private var instance: Repository? = null
        fun getInstance(remoteSource: RemoteSource, localSource: LocalSource):Repository{
            return instance?: synchronized(this){
                val temp=Repository(remoteSource,localSource)
                instance=temp
                temp
            }
        }
    }

    override suspend fun getCurrentWeather(lat: Double,
                                           lon: Double,
                                           lang: String,
                                           units: String,
                                           exclude: String): Forecast {
        return remoteSource.getCurrentWeather(33.44,-94.04)
    }





}