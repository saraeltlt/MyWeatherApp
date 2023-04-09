package com.example.myweatherapp.utils

import com.example.myweatherapp.R
import com.example.myweatherapp.model.MyPref

object Constant {
    const val API_KEY= "416c3f7d60f73a4f8f76c658c93cf3b7" //"04140387a0c9b9535e981afe74e9ac2a"
    const val LOCATION_PERMISSION_REQUEST_CODE = 2000
    const val REQUEST_CODE = 4004
    var myPref = MyPref()

}
class MyIcons{
    companion object{
        var mapIcon = HashMap<String, Int>()
        init {
            mapIcon["01d"]= R.drawable.d01
            mapIcon["02d"]=R.drawable.d02
            mapIcon["03d"]=R.drawable.d03
            mapIcon["04d"]=R.drawable.d04
            mapIcon["09d"]=R.drawable.d09
            mapIcon["10d"]=R.drawable.d10
            mapIcon["11d"]=R.drawable.d11
            mapIcon["13d"]=R.drawable.d13
            mapIcon["50d"]=R.drawable.d50

            mapIcon["01n"]=R.drawable.n01
            mapIcon["02n"]=R.drawable.n02
            mapIcon["03n"]=R.drawable.n03
            mapIcon["04n"]=R.drawable.n04
            mapIcon["09n"]=R.drawable.n09
            mapIcon["10n"]=R.drawable.n10
            mapIcon["11n"]=R.drawable.n11
            mapIcon["13n"]=R.drawable.n13
            mapIcon["50n"]=R.drawable.n50
        }

    }
}
