package com.example.myweatherapp.utils

import android.content.Context
import com.example.myweatherapp.startPref.model.MyPref
import com.google.gson.Gson

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


}