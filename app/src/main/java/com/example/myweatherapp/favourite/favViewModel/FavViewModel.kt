package com.example.myweatherapp.favourite.favViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.model.Forecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavViewModel(private val _repo : RepoInterface): ViewModel() {
    private var _weather : MutableLiveData<List<Forecast>> =MutableLiveData<List<Forecast>>()
    val weather: LiveData<List<Forecast>> = _weather


}