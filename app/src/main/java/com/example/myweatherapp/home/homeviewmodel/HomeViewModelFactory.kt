package com.example.myweatherapp.home.homeviewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.location.GPSProvider
import com.example.myweatherapp.utils.Constant

class HomeViewModelFactory (private val _repo : RepoInterface, val myGps: GPSProvider,
                            val context: Context, val lang: String=Constant.appDefaultLanguage, val units: String=Constant.appDefaultUnit) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(_repo,context,myGps,lang,units) as T
        }else{
            throw java.lang.IllegalArgumentException("ViewModel class not found")
        }
    }
}