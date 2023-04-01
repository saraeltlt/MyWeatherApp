package com.example.myweatherapp.datasource

import com.example.myweatherapp.datasource.db.LocalSource
import com.example.myweatherapp.datasource.network.RemoteSource
import com.example.myweatherapp.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow


class FakeDataSource ( var favWeather: MutableList<Forecast> =  mutableListOf()): LocalSource, RemoteSource {

    fun generateDummyData(): Forecast {
        val alert1 = Alert(
            "A severe thunderstorm warning is in effect",
            "Severe Thunderstorm Warning"
        )

        val hourly1 = Hourly(
            75,
            1648686000,
            80,
            1005,
            26.5,
            2.3,
            28,
            listOf(Weather("Overcast", "04d", 804, "Clouds")),
            4.6
        )

        val temp1 = Temp(
            28.5,
            26.7,
            30.2,
            25.5,
            26.1,
            29.9
        )

        val daily1 = Daily(
            65,
            1648838400,
            75,
            1010,
            0.0,
            0.0,
            temp1,
            7.1,
            listOf(Weather("Clear", "01d", 800, "Clear Sky")),
            3.5
        )

        val current1 = Current(
            30,
            1648671600,
            50,
            1012,
            22.6,
            6.7,
            10000,
            listOf(Weather("Clear", "01d", 800, "Clear Sky")),
            2.8
        )

        var forecast = Forecast(
            listOf(alert1),
            current1,
            listOf(daily1),
            listOf(hourly1),
            37.7749,
            -122.4194,
            "America/Los_Angeles",
            -25200
        )

        val weather1 = Weather(
            "Overcast",
            "04d",
            804,
            "Clouds"
        )


        return forecast

    }
    //local source under test

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

    //remote source under test
    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String
    ): StateFlow<Forecast> {
        var stateFlow = MutableStateFlow(generateDummyData()) as StateFlow<Forecast>
        return stateFlow
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