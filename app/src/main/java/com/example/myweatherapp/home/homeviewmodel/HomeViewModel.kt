package com.example.myweatherapp.home.homeviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.model.Forecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val _repo : RepoInterface): ViewModel() {
    private var _weather : MutableLiveData<Forecast> = MutableLiveData<Forecast>()
    val weather: LiveData<Forecast> = _weather


    init {
        getCurrentWeather(33.44,-94.04)
    }
    fun  getCurrentWeather(lat: Double,
                           lon: Double,
                           lang: String="en",
                           units: String="metric",exclude: String = "minutely"){
        viewModelScope.launch(Dispatchers.IO) {
            _weather.postValue(_repo.getCurrentWeather(lat,lon))
        }
    }

}