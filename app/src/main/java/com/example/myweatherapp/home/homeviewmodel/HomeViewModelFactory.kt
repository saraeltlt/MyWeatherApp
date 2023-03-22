package com.example.myweatherapp.home.homeviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.datasource.RepoInterface

class HomeViewModelFactory (private val _repo : RepoInterface, val lat: Double, val lon: Double, val lang: String, val units: String) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(_repo,lat,lon, lang,units) as T
        }else{
            throw java.lang.IllegalArgumentException("ViewModel class not found")
        }
    }
}