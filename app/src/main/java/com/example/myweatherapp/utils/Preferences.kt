package com.example.myweatherapp.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.example.myweatherapp.model.MyPref
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
         val localeNew = Locale(lng)
         Locale.setDefault(localeNew)
         val res: Resources = context.getResources()
         val newConfig = Configuration(res.getConfiguration())
         newConfig.locale = localeNew
         newConfig.setLayoutDirection(localeNew)
         res.updateConfiguration(newConfig, res.getDisplayMetrics())
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
             newConfig.setLocale(localeNew)
             context.createConfigurationContext(newConfig)
         }
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