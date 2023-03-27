package com.example.myweatherapp.notifications.notificationviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.datasource.RepoInterface
import com.example.myweatherapp.notifications.notificationmodel.Alert

class NotificationViewModel(private val _repo : RepoInterface): ViewModel() {
    private var _alert : MutableLiveData<List<Alert>> =MutableLiveData<List<Alert>>()
    val alert: LiveData<List<Alert>> = _alert


}