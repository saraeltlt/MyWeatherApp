package com.example.myweatherapp.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.myweatherapp.R
import com.example.myweatherapp.onboarding.screens.OnBoarding1Fragment
import com.example.myweatherapp.onboarding.screens.OnBoarding2Fragment
import com.example.myweatherapp.onboarding.screens.OnBoarding3Fragment


class ViewPagerFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view =inflater.inflate(R.layout.fragment_view_pager, container, false)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        val fragmentList = arrayListOf<Fragment>(
            OnBoarding1Fragment(),
            OnBoarding2Fragment(),
            OnBoarding3Fragment()
        )
        val adapter = ViewPagerAdapter(fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        viewPager.adapter=adapter
        return view;
    }


}