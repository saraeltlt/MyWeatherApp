package com.example.myweatherapp.home.homeview

class TimeAdapter (var action: OnTimeClickListner)  {
}
interface OnTimeClickListner {
    fun onDayClick()
}