package com.example.myweatherapp.home.homeviewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.location.GPSProvider
import com.example.myweatherapp.utils.Constant
import com.google.android.gms.maps.model.LatLng

class HomeViewModelFactory (private val _repo : RepoInterface, val myLoc:LatLng,
                             val lang: String=Constant.myPref.appLanguage, val units: String=Constant.myPref.appUnit) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(_repo,myLoc,lang,units) as T
        }else{
            throw java.lang.IllegalArgumentException("ViewModel class not found")
        }
    }
}