package com.example.myweatherapp.utils

import androidx.room.TypeConverter
import com.example.myweatherapp.model.Alert
import com.example.myweatherapp.model.Current
import com.example.myweatherapp.model.Daily
import com.example.myweatherapp.model.Hourly
import com.example.myweatherapp.model.MyAlert

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun stringToMyAlertList(value: String?): List<MyAlert?>? {
        val listType = object : TypeToken<ArrayList<MyAlert?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun myAlertListToString(list: List<MyAlert?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
    @TypeConverter
    fun stringToMyAlert(value: String?): MyAlert? {
        return Gson().fromJson(value, MyAlert::class.java)
    }

    @TypeConverter
    fun myAlertToString(value: MyAlert): String? {
        return Gson().toJson(value)
    }


    @TypeConverter
    fun stringToAlertList(value: String?): List<Alert?>? {
        val listType = object : TypeToken<ArrayList<Alert?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun alertListToString(list: List<Alert?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
    @TypeConverter
    fun stringToAlert(value: String?): Alert? {
        return Gson().fromJson(value, Alert::class.java)
    }

    @TypeConverter
    fun alertToString(value: Alert): String? {
        return Gson().toJson(value)
    }






    @TypeConverter
    fun stringToCurrent(value: String?): Current? {
        return Gson().fromJson(value, Current::class.java)
    }

    @TypeConverter
    fun currentToString(value: Current): String? {
        return Gson().toJson(value)
    }
    @TypeConverter
    fun stringToDaily(value: String?): Daily? {
        return Gson().fromJson(value, Daily::class.java)
    }

    @TypeConverter
    fun DailyToString(value: Daily): String? {
        return Gson().toJson(value)
    }
    @TypeConverter
    fun stringToHourly(value: String?): Hourly? {
        return Gson().fromJson(value, Hourly::class.java)
    }

    @TypeConverter
    fun hourlyToString(value: Hourly): String? {
        return Gson().toJson(value)
    }

        @TypeConverter
        fun fromCuurentList(list: List<Current?>?): String? {
            if (list == null) {
                return null
            }
            val gson = Gson()
            val type= object :
                TypeToken<List<Current?>?>() {}.type
            return gson.toJson(list, type)
        }

        @TypeConverter
        fun toCurrentList(item: String?): List<Current>? {
            if (item == null) {
                return null
            }
            val gson = Gson()
            val type = object :
                TypeToken<List<Current?>?>() {}.type
            return gson.fromJson<List<Current>>(item, type)
        }


    @TypeConverter
    fun fromDailyList(list: List<Daily?>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type= object :
            TypeToken<List<Daily?>?>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toDailyList(item: String?): List<Daily>? {
        if (item == null) {
            return null
        }
        val gson = Gson()
        val type = object :
            TypeToken<List<Daily?>?>() {}.type
        return gson.fromJson<List<Daily>>(item, type)
    }

    @TypeConverter
    fun fromHourlyList(list: List<Hourly?>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type= object :
            TypeToken<List<Hourly?>?>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toHourlyList(item: String?): List<Hourly>? {
        if (item == null) {
            return null
        }
        val gson = Gson()
        val type = object :
            TypeToken<List<Hourly?>?>() {}.type
        return gson.fromJson<List<Hourly>>(item, type)
    }


}