package com.example.myweatherapp.favourite.favViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.datasource.RepoInterface

class FavViewModelFactory (private val _repo : RepoInterface) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavViewModel::class.java)){
            FavViewModel(_repo) as T
        }else{
            throw java.lang.IllegalArgumentException("ViewModel class not found")
        }
    }
}