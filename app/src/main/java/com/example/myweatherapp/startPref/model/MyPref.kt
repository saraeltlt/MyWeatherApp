package com.example.myweatherapp.startPref.model

import com.google.android.gms.maps.model.LatLng

data class MyPref(var appLanguage: String = "en",
             var appUnit: String ="metric",
             var appMode: String = "dark",
             var myLocation: LatLng =LatLng(0.0,0.0)
) {
}