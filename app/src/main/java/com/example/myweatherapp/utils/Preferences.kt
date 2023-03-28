package com.example.myweatherapp.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.myweatherapp.startPref.model.MyPref
import com.google.gson.Gson
import java.util.*

object Preferences{

   fun saveMyPref(myPref: MyPref, context: Context) {
        val mPrefs = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val prefsEditor = mPrefs.edit()
        val json = Gson().toJson(myPref)
        prefsEditor.putString("MyPref", json)
        prefsEditor.commit()
    }

    fun getMyPref(context: Context): MyPref {
        val mPrefs = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val jsonDefault = Gson().toJson(Constant.myPref)
        val json = mPrefs.getString("MyPref", jsonDefault).toString()
        val obj: MyPref = Gson().fromJson(json, MyPref::class.java)
        return obj;
    }
     fun setLocale(lng: String, context: Context) {
        val res = context.resources
        val metric = res.displayMetrics
        val config = res.configuration
        config.locale = Locale(lng)
        res.updateConfiguration(config,metric)
    }
    fun setMood(mood:String){
        if (mood=="dark") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


}