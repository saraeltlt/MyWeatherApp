package com.example.myweatherapp.ui.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.example.myweatherapp.R
import com.example.myweatherapp.model.MyPref
import com.example.myweatherapp.utils.Preferences
import com.google.android.material.bottomnavigation.BottomNavigationView


class OnBoarding1Fragment : Fragment() {
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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_boarding1, container, false)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        val myPref = MyPref()
        Preferences.saveMyPref(myPref,requireContext())
        val button = view.findViewById<Button>(R.id.next)
        button.setOnClickListener {
            viewPager?.currentItem=1

        }
        return view
    }

}