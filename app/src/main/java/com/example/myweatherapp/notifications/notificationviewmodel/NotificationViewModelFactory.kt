package com.example.myweatherapp.notifications.notificationviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.datasource.RepoInterface

class NotificationViewModelFactory (private val _repo : RepoInterface) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NotificationViewModel::class.java)){
            NotificationViewModel() as T
        }else{
            throw java.lang.IllegalArgumentException("ViewModel class not found")
        }
    }
}