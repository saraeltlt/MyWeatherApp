package com.example.myweatherapp.datasource.network

import com.example.myweatherapp.model.Forecast



class ApiClient private constructor(): RemoteSource {


companion object{
    var single = ApiClient()

    fun getInstance(): ApiClient {
        if (single == null)
            single = ApiClient()
        return single
    }

}

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String,
        exclude: String,
        appid: String
    ): Forecast {
        val obj= RetrofitHelper.retrofit.create(ApiServer::class.java)
        return obj.getCurrentWeatherByLatAndLon(lat,lon,lang,units,exclude,appid)
    }
}