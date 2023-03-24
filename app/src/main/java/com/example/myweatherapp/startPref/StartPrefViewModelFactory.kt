package com.example.myweatherapp.startPref

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.datasource.Repository


class StartPrefViewModelFactory (private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(StartPrefViewModel::class.java)){
            StartPrefViewModel(repository) as T
        } else{
            throw java.lang.IllegalArgumentException("InitialPreferencesViewModel class not found")
        }
    }
}