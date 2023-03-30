package com.example.myweatherapp.datasource.db

import android.content.Context
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.model.MyAlert
import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource private constructor(context: Context) : LocalSource{
    val dp: WeatherDB= WeatherDB.getInstance(context)
    private val dao: WeatherDAO by lazy{
        dp.weatherDao()
    }
    private val daoAlert: AlertDAO by lazy{
        dp.AlertDao()
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



    override suspend fun getAllAlerts(): Flow<List<MyAlert> >{
        return daoAlert.getAllAlerts()
    }

    override suspend fun insertAlert(myAlert: MyAlert) {
        daoAlert.insertAlert(myAlert)
    }

    override suspend fun deleteAlert(myAlert: MyAlert) {
        daoAlert.deleteAlert(myAlert)
    }


}