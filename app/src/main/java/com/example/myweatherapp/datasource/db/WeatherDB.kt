package com.example.myweatherapp.datasource.db

import android.content.Context
import androidx.room.*
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.notifications.notificationmodel.Alert
import com.example.myweatherapp.utils.Converters


@Database(entities = [Forecast::class, Alert::class], version = 1 , exportSchema = false)
@TypeConverters(value = [Converters::class])
abstract class WeatherDB : RoomDatabase() {

    abstract fun weatherDao(): WeatherDAO

    companion object {
        @Volatile
        private var instance: WeatherDB? = null
        fun getInstance(context: Context): WeatherDB {
            return instance?: synchronized(this){
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDB::class.java, "Weather.db"
                ).build()
                instance=inst
                inst}

        }
    }

}