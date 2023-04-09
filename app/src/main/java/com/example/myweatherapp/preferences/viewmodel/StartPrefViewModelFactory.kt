package com.example.myweatherapp.preferences.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.location.GPSProvider


class StartPrefViewModelFactory (val context: Context, val myGps: GPSProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(StartPrefViewModel::class.java)){
            StartPrefViewModel(context, myGps) as T
        } else{
            throw java.lang.IllegalArgumentException("InitialPreferencesViewModel class not found")
        }
    }
}