package com.example.myweatherapp.home.homeviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.model.ApiState
import com.example.myweatherapp.utils.NetworkManager
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val _repo : RepoInterface, val myLoc: LatLng, val lang: String, val units: String): ViewModel() {
    private var _weather : MutableLiveData<Forecast> = MutableLiveData<Forecast>()
    val weather: LiveData<Forecast> = _weather
    private var _stateFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    val stateFlow: StateFlow<ApiState> = _stateFlow
    init {
        if(NetworkManager.isInternetConnected()) {
            getCurrentWeatherRemote(myLoc.latitude,myLoc.longitude,lang,units)
        }
    }

    fun  getCurrentWeatherRemote(lat: Double,
                                 lon: Double,
                                 lang: String,
                                 units: String){


        viewModelScope.launch(Dispatchers.IO) {
            _repo.getCurrentWeather(lat,lon,lang,units).catch { e ->
                _stateFlow.value = ApiState.Failure(e)
            }.collect { data ->
                _stateFlow.value = ApiState.Succcess(data)
                _weather.postValue(data)
            }

        }
      /*  viewModelScope.launch(Dispatchers.IO) {
            _weather.postValue(_repo.getCurrentWeather(lat,lon,lang,units))
        }*/
    }


    fun  getCurrentWeatherFromDB(){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getWeatherDataFromDB().collect(){
            _weather.postValue(it)
                }
        }
    }

    fun addWeather(forecat:Forecast){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertWeatherData(forecat)
        }

    }








}