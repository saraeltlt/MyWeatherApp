package com.example.myweatherapp.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myweatherapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class SplashFragment : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNav=requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.visibility= View.GONE
     val viewLine=requireActivity().findViewById<View>(R.id.viewLine)
        viewLine.visibility= View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Handler().postDelayed({
            if (onBoardingFinished()){
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)


            }else{
                findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
            }

        },4500)

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
    private fun onBoardingFinished() : Boolean{
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
       return sharedPref.getBoolean("finished",false)
    }

}