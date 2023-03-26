package com.example.myweatherapp.utils

import android.app.Application
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceManager
import com.example.myweatherapp.datasource.Repository
import com.example.myweatherapp.datasource.db.ConcreteLocalSource
import com.example.myweatherapp.datasource.network.ClientRemoteSource


class MyApp : Application() {
    companion object {

        private var localDataSource: ConcreteLocalSource? = null
        private var remoteDataSource: ClientRemoteSource? = null
        private var sharedPreferences : SharedPreferences? = null
        private val repository by lazy { Repository( remoteDataSource!!,
            localDataSource!!) }


        @Synchronized
        fun getInstanceRepository(): Repository {
            if (repository == null) {
                throw IllegalStateException("Repository not initialized")
            }
            return repository!!
        }

        @Synchronized
        fun getInstanceSharedPreferences(): SharedPreferences {
            if (sharedPreferences == null) {
                throw IllegalStateException("SharedPreferences not initialized")
            }
            return sharedPreferences!!
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()

        localDataSource = ConcreteLocalSource.getInstance(this)
        remoteDataSource = ClientRemoteSource.getInstance(this)
        NetworkManager.init(this)


    }
}