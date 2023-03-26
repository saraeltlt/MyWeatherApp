package com.example.myweatherapp.startPref.viewmodel


import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.location.GPSProvider
import com.google.android.gms.maps.model.LatLng

class StartPrefViewModel(val context: Context) : ViewModel() {
    private var _location : MutableLiveData<LatLng> = MutableLiveData<LatLng>()
    val location: LiveData<LatLng> = _location
    init {
        getLocationFromGPS(context)
    }
    fun getLocationFromGPS (context: Context) {
        val myGps = GPSProvider(context)
        myGps.getCurrentLocation()
        myGps.data.observe(context as LifecycleOwner) {
            _location.postValue(it)
        }
    }



}





