package com.example.myweatherapp.provider

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myweatherapp.utils.Constant
import com.google.android.gms.location.*

class GPSProvider(var context: Context) {
    private lateinit var fusedClient: FusedLocationProviderClient
    private var _data : MutableLiveData<LocationDetails> = MutableLiveData<LocationDetails>()
    val data: LiveData<LocationDetails> = _data
    fun getCurrentLocation(){
        if (checkPremission()){
            if (isLocationEnabled()){
                requestNewLocationData()
            }
            else{

                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                ContextCompat.startActivity(context, intent, null)
            }
        }
        else{
            requestPermission()

        }
    }

    private fun checkPremission():Boolean{
        val result = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        return result
    }
    private fun isLocationEnabled():Boolean{ //law ay provider 3andk enabled
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    @SuppressLint("MissingPermission")
    fun requestNewLocationData(){
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority= LocationRequest.PRIORITY_HIGH_ACCURACY
       // mLocationRequest.interval= Constant.ONE_MINUTE
       // mLocationRequest.fastestInterval=Constant.ONE_MINUTE/4
        fusedClient= LocationServices.getFusedLocationProviderClient(context)
        fusedClient.requestLocationUpdates(
            mLocationRequest,mLocationCallback, Looper.myLooper())
    }
    private val mLocationCallback: LocationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            val result : Location? = p0.lastLocation
            _data.postValue(LocationDetails(result!!.latitude, result!!.longitude))


        }
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(context as Activity, arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ), Constant.LOCATION_PERMISSION_REQUEST_CODE)
    }


}