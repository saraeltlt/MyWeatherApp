package com.example.myweatherapp.model

sealed class ApiState{
    class Succcess(val data : Forecast): ApiState()
    class Failure(val msg : Throwable): ApiState()
    object Loading: ApiState()
}
