package com.example.myweatherapp.datasource.db

import android.content.Context
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.notifications.notificationmodel.Alert

class ConcreteLocalSource private constructor(context: Context) : LocalSource{

    private val dao: WeatherDAO by lazy{
        val dp: WeatherDB= WeatherDB.getInstance(context)
        dp.weatherDao()
    }
    companion object{
        @Volatile
        private var instance: ConcreteLocalSource? = null
        @Synchronized
        fun getInstance(context: Context): ConcreteLocalSource {
            if(instance == null){
                instance = ConcreteLocalSource(context)
            }
            return instance!!
        }
    }
    override suspend fun getWeatherDataFromDB(): Forecast {
        return dao.getWeatherDataFromDB(1)
    }


    override suspend fun deleteCurrentWeather() {
        dao.deleteWeather(1)
    }

    override suspend fun getAllFav(): List<Forecast> {
        return dao.getAllFav()
    }

    override suspend fun insertFavOrCurrent(forecast: Forecast) {
        dao.insertFavOrCurrent(forecast)
    }

    override suspend fun deleteFav(forecast: Forecast) {
        dao.deleteFav(forecast)
    }



    override suspend fun getAllAlerts(): List<Alert> {
        return dao.getAllAlerts()
    }

    override suspend fun insertAlert(alert: Alert) {
        dao.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        dao.deleteAlert(alert)
    }


}