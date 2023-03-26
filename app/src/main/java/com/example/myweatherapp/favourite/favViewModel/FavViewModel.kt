package com.example.myweatherapp.favourite.favViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavViewModel(private val _repo : RepoInterface): ViewModel() {
    private var _weather : MutableLiveData<List<Forecast>> =MutableLiveData<List<Forecast>>()
    val weather: LiveData<List<Forecast>> = _weather
    init{
        getAllFav()
    }
    fun  getAllFav(){
        viewModelScope.launch(Dispatchers.IO) {
            _weather.postValue(_repo.getAllFav())
        }
    }

    fun deleteFav(forecast: Forecast){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.deleteFav(forecast)
           getAllFav()
        }

    }
    fun addFav(forecast: Forecast){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertFav(forecast)
            getAllFav()
        }
    }
    fun  getFavRemote(lat: Double,
                                 lon: Double,
                                 lang: String = Constant.appDefaultLanguage,
                                 units: String= Constant.appDefaultUnit){
        viewModelScope.launch(Dispatchers.IO) {
          val favItem=  _repo.getCurrentWeather(lat,lon,lang,units)
            addFav(favItem)
        }
    }


}