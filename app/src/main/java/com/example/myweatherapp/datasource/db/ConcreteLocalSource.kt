package com.example.myweatherapp.datasource.db

import android.content.Context
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.notifications.notificationmodel.Alert
import kotlinx.coroutines.flow.Flow

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
    override suspend fun getWeatherDataFromDB(): Flow<Forecast>{
        return dao.getWeatherDataFromDB()
    }


    override suspend fun deleteCurrentWeather() {
        dao.deleteWeather()
    }

    override suspend fun getAllFav(): Flow<List<Forecast>> {
        return dao.getAllFav()
    }

    override suspend fun insertFavOrCurrent(forecast: Forecast) {
        dao.insertFavOrCurrent(forecast)
    }

    override suspend fun deleteFav(forecast: Forecast) {
        dao.deleteFav(forecast)
    }



    override suspend fun getAllAlerts(): Flow<List<Alert> >{
        return dao.getAllAlerts()
    }

    override suspend fun insertAlert(alert: Alert) {
        dao.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        dao.deleteAlert(alert)
    }


}