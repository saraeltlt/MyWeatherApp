package com.example.myweatherapp.notifications.notificationviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.model.MyAlert

class NotificationViewModel(private val _repo : RepoInterface): ViewModel() {
    private var _My_alert : MutableLiveData<List<MyAlert>> =MutableLiveData<List<MyAlert>>()
    val myAlert: LiveData<List<MyAlert>> = _My_alert


}