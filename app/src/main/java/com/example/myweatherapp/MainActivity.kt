package com.example.myweatherapp

import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.myweatherapp.databinding.ActivityMainBinding
import com.example.myweatherapp.startPref.model.MyPref
import com.example.myweatherapp.utils.Constant
import com.example.myweatherapp.utils.Preferences
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        var mypref = Preferences.getMyPref(this)
        Constant.myPref=mypref
        Preferences.setLocale(Constant.myPref.appLanguage, this)
        Preferences.setMood(mypref.appMode)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNav, navController)






    }





    }