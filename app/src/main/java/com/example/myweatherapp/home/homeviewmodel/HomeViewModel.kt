package com.example.myweatherapp.home.homeviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.model.Forecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val _repo : RepoInterface, lat: Double, lon: Double,lang: String,units: String): ViewModel() {
    private var _weather : MutableLiveData<Forecast> = MutableLiveData<Forecast>()
    val weather: LiveData<Forecast> = _weather


    init {
        getCurrentWeather(lat,lon,lang,units)
    }
    fun  getCurrentWeather(lat: Double,
                           lon: Double,
                           lang: String="en",
                           units: String="metric"){
        viewModelScope.launch(Dispatchers.IO) {
            _weather.postValue(_repo.getCurrentWeather(lat,lon,lang,units))
        }
    }

}