package com.example.myweatherapp.favourite.favViewModel

import androidx.lifecycle.*
import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.model.ApiState
import com.example.myweatherapp.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavViewModel(private val _repo : RepoInterface): ViewModel() {
    private var _favList : MutableLiveData<List<Forecast>> =MutableLiveData<List<Forecast>>()
    val favList: LiveData<List<Forecast>> = _favList
    private var _stateFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    val stateFlow: StateFlow<ApiState> = _stateFlow
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
           getAllFav()
        }
    }
    fun  getFavRemote(lat: Double,
                      lon: Double,
                      lang: String = Constant.myPref.appLanguage,
                      units: String= Constant.myPref.appUnit) {

        viewModelScope.launch(Dispatchers.IO) {
          // favItem=  _repo.getCurrentWeather(lat,lon,lang,units)
             _repo.getCurrentWeather(lat,lon,lang,units).catch { e ->
                _stateFlow.value = ApiState.Failure(e)
            }.collect { data ->
                _stateFlow.value = ApiState.Succcess(data)
                 addFav(data)
            }

        }

    }


}