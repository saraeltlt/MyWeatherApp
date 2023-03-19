package com.example.myweatherapp.home.homeview

import androidx.recyclerview.widget.ListAdapter

class DaysAdapter (var action: OnDayClickListner) {

}

interface OnDayClickListner {
    fun onDayClick()
}