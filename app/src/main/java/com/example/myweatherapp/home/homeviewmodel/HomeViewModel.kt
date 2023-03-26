package com.example.myweatherapp.home.homeviewmodel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.location.GPSProvider
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val _repo : RepoInterface, val myLoc: LatLng, val lang: String, val units: String): ViewModel() {
    private var _weather : MutableLiveData<Forecast> = MutableLiveData<Forecast>()
    val weather: LiveData<Forecast> = _weather
    init {
        getCurrentWeatherRemote(myLoc.latitude,myLoc.longitude,lang,units)
    }

    fun  getCurrentWeatherRemote(lat: Double,
                                 lon: Double,
                                 lang: String,
                                 units: String){
        viewModelScope.launch(Dispatchers.IO) {
            _weather.postValue(_repo.getCurrentWeather(lat,lon,lang,units))
        }
    }


    fun  getCurrentWeatherFromDB(){
        viewModelScope.launch(Dispatchers.IO) {
            _weather.postValue(_repo.getWeatherDataFromDB())
        }
    }

    fun addWeather(forecat:Forecast){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertWeatherData(forecat)
        }

    }







}