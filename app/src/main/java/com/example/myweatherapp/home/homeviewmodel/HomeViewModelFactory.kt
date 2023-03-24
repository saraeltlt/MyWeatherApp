package com.example.myweatherapp.home.homeviewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.provider.GPSProvider

class HomeViewModelFactory (private val _repo : RepoInterface, val myGps: GPSProvider,
                            val context: Context, val lang: String="en", val units: String="metric") : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(_repo,context,myGps,lang,units) as T
        }else{
            throw java.lang.IllegalArgumentException("ViewModel class not found")
        }
    }
}