package com.example.myweatherapp.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myweatherapp.utils.Constant
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

class GPSProvider(var context: Context) {
    private lateinit var fusedClient: FusedLocationProviderClient
    private var _data : MutableLiveData<LatLng> = MutableLiveData<LatLng>()
    val data: LiveData<LatLng> = _data
    fun getCurrentLocations(){
        requestNewLocationData()
    }

    @SuppressLint("MissingPermission")
    fun requestNewLocationData(){
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority= LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedClient= LocationServices.getFusedLocationProviderClient(context)
        fusedClient.requestLocationUpdates(
            mLocationRequest,mLocationCallback, Looper.myLooper())
    }
    private val mLocationCallback: LocationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            val result : Location? = p0.lastLocation
            _data.postValue(LatLng(result!!.latitude, result!!.longitude))


        }
    }





}