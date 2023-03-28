package com.example.myweatherapp.datasource.network

import android.content.Context
import com.example.myweatherapp.model.Forecast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


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
    ): StateFlow<Forecast>{
         var forecast : Forecast? = null

        val obj= RetrofitHelper.retrofit.create(ApiServer::class.java)
         val response = obj.getCurrentWeatherByLatAndLon(lat,lon,lang,units)
        if (response.isSuccessful){
            forecast= response.body()!!
        }
        var stateFlow= MutableStateFlow(forecast) as StateFlow<Forecast>
        return stateFlow
    }
}