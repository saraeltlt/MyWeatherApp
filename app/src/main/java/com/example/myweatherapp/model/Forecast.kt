package com.example.myweatherapp.model

import android.os.Parcelable
import androidx.room.Entity

import kotlinx.parcelize.Parcelize



@Entity(tableName = "favorites", primaryKeys = ["lat","lon","currentWeather"])

@Parcelize
data class Forecast(
   // val alerts: List<Alert>,
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
    val clouds: Int,//need
    val dew_point: Double,
    val dt: Int,//need
    val feels_like: Double,
    val humidity: Int,//need
    val pressure: Int,//need
    //val rain: Rain,
    val sunrise: Int,
    val sunset: Int,

    val temp: Double,//need
    val uvi: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_speed: Double//need
): Parcelable
@Parcelize
data class Daily(
    val clouds: Int,//need
    val dew_point: Double,
    val dt: Int,
    val feels_like: FeelsLike,
    val humidity: Int,//need
    val moon_phase: Double,
    val moonrise: Int,
    val moonset: Int,
    val pop: Double,
    val pressure: Int,//need
    val rain: Double,
    val snow: Double,
    val sunrise: Int,
    val sunset: Int,
    val temp: Temp,//need
    val uvi: Double,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double//need
): Parcelable
@Parcelize
data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
): Parcelable
@Parcelize
data class Hourly(
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,//need
    val feels_like: Double,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
 //   val rain: RainX,
   // val snow: Snow,
    val temp: Double,//need
    val uvi: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
): Parcelable

/*@Parcelize
data class Rain(
    val `1h`: Double
): Parcelable
@Parcelize
data class RainX(
    val `1h`: Double
): Parcelable
@Parcelize
data class Snow(
    val `1h`: Double
): Parcelable*/
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