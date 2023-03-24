package com.example.myweatherapp.datasource.network

import android.content.Context
import com.example.myweatherapp.model.Forecast



class ClientRemoteSource private constructor(context: Context) : RemoteSource {


    companion object {
        @Volatile
        private var instance: ClientRemoteSource? = null

        @Synchronized
        fun getInstance(context: Context): ClientRemoteSource {
            if (instance == null) {
                instance = ClientRemoteSource(context)
            }
            return instance!!
        }
    }

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String,
    ): Forecast {
        val obj= RetrofitHelper.retrofit.create(ApiServer::class.java)
        return obj.getCurrentWeatherByLatAndLon(lat,lon,lang,units)
    }
}