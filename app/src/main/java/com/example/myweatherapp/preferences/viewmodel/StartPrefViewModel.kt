package com.example.myweatherapp.preferences.viewmodel


import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.location.GPSProvider
import com.google.android.gms.maps.model.LatLng

class StartPrefViewModel(val context: Context, var myGps: GPSProvider) : ViewModel() {
    private var _location : MutableLiveData<LatLng> = MutableLiveData<LatLng>()
    val location: LiveData<LatLng> = _location
    init {
        getLocationFromGPS(context)
    }
    fun getLocationFromGPS (context: Context) {
       // myGps = GPSProvider(context)
        myGps.getCurrentLocations()
        myGps.data.observe(context as LifecycleOwner) {
            _location.postValue(it)
        }
    }



}





