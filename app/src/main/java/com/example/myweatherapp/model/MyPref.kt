package com.example.myweatherapp.model

import com.google.android.gms.maps.model.LatLng

data class MyPref(var appLanguage: String = "en",
             var appUnit: String ="standard",
             var appMode: String = "dark",
             var myLocation: LatLng =LatLng(0.0,0.0),
                  var alarmFlag:Boolean = true
) {
}