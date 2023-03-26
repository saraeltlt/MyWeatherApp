package com.example.myweatherapp

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.myweatherapp.databinding.ActivityMainBinding
import com.example.myweatherapp.utils.Constant
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadSettings()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNav, navController)





    }
    private fun setLocale(lng: String) {
        val locale = Locale(lng)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources
            .updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    private fun loadSettings()
    {
        val sp= PreferenceManager.getDefaultSharedPreferences(this)
        val languageKey=sp.getString("language","")
        setLocale(languageKey!!)
        Constant.myPref.appLanguage = languageKey
    }

}