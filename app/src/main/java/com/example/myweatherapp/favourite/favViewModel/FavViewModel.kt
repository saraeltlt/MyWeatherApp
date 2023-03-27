package com.example.myweatherapp.favourite.favViewModel

import androidx.lifecycle.*
import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.notifications.notificationmodel.Alert
import com.example.myweatherapp.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavViewModel(private val _repo : RepoInterface): ViewModel() {
    private var _favList : MutableLiveData<List<Forecast>> =MutableLiveData<List<Forecast>>()
    val favList: LiveData<List<Forecast>> = _favList
    lateinit var favItem:Forecast
    init {
        getAllFav()
    }

   fun  getAllFav() {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getAllFav().collect(){
                _favList.postValue(it)
            }
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
           // getAllFav()
        }
    }
    fun  getFavRemote(lat: Double,
                      lon: Double,
                      lang: String = Constant.myPref.appLanguage,
                      units: String= Constant.myPref.appUnit) {

        viewModelScope.launch(Dispatchers.IO) {
           favItem=  _repo.getCurrentWeather(lat,lon,lang,units)
            addFav(favItem)
        }

    }


}