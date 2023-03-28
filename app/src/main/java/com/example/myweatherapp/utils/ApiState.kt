package com.example.myweatherapp.utils

import com.example.myweatherapp.model.Forecast

sealed class ApiState{
    class Succcess(val data : Forecast): ApiState()
    class Failure(val msg : Throwable): ApiState()
    object Loading: ApiState()
}
