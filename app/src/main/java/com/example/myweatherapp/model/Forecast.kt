package com.example.myweatherapp.model
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

import kotlinx.parcelize.Parcelize



@Entity(tableName = "favorites", primaryKeys = ["lat","lon","currentWeather"])

@Parcelize
data class Forecast(
    var alerts: List<Alert> = listOf(),
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    var currentWeather: Int =0
): Parcelable

@Parcelize
data class Current(
    val clouds: Int,
    val dt: Int,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind_speed: Double
): Parcelable
@Parcelize
data class Alert(
    val description: String="",
    val event: String=""
): Parcelable{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
@Parcelize
data class Daily(
    val clouds: Int,
    val dt: Int,
    val humidity: Int,
    val pressure: Int,
    val rain: Double,
    val snow: Double,
    val temp: Temp,
    val uvi: Double,
    val weather: List<Weather>,
    val wind_speed: Double
): Parcelable

@Parcelize
data class Hourly(
    val clouds: Int,
    val dt: Int,//need
    val humidity: Int,
    val pressure: Int,
    val temp: Double,//need
    val uvi: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind_speed: Double
): Parcelable

@Parcelize
data class Temp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
): Parcelable
@Parcelize
data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
): Parcelable