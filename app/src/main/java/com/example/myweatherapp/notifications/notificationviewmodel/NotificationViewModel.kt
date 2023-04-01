package com.example.myweatherapp.notifications.notificationviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.model.ApiState
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.model.MyAlert
import com.example.myweatherapp.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class NotificationViewModel(private val _repo : RepoInterface): ViewModel() {
    private var _My_alert : MutableLiveData<List<MyAlert>> =MutableLiveData<List<MyAlert>>()
    val myAlert: LiveData<List<MyAlert>> = _My_alert
    private var _stateFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    val stateFlow: StateFlow<ApiState> = _stateFlow

    init {
        getAllAlert()
    }

    fun  getAllAlert() {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getAllAlerts().collect(){
                _My_alert.postValue(it)
            }
        }

    }

    fun deleteAlert(myAlert: MyAlert){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.deleteAlert(myAlert)
            getAllAlert()
        }

    }
    fun addAlert(myAlert: MyAlert){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertAlert(myAlert)
            getAllAlert()
        }
    }
    fun  getAlertRemote(lat: Double = Constant.myPref.myLocation.latitude,
                      lon: Double=Constant.myPref.myLocation.longitude,
                      lang: String = Constant.myPref.appLanguage,
                      units: String= Constant.myPref.appUnit) {

        viewModelScope.launch(Dispatchers.IO) {
            _repo.getCurrentWeather(lat,lon,lang,units).catch { e ->
                _stateFlow.value = ApiState.Failure(e)
            }.collect { data ->
                _stateFlow.value = ApiState.Succcess(data)
            }

        }

    }


}