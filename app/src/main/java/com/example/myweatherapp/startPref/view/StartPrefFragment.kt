package com.example.myweatherapp.startPref.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentStartPrefBinding
import com.example.myweatherapp.location.GPSProvider

import com.example.myweatherapp.startPref.viewmodel.StartPrefViewModel
import com.example.myweatherapp.startPref.viewmodel.StartPrefViewModelFactory
import com.example.myweatherapp.utils.Constant

import com.example.myweatherapp.utils.NetworkManager
import com.example.myweatherapp.utils.Preferences
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.util.*


class StartPrefFragment : Fragment() {
    private lateinit var binding: FragmentStartPrefBinding
    private var myLocation: LatLng? = null
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
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_start_pref,container,false) as FragmentStartPrefBinding
        binding.lifecycleOwner=this
        val view = binding.root
        val factory = StartPrefViewModelFactory(requireContext(),  GPSProvider(requireContext()))
        val viewModel =
            ViewModelProvider(this, factory).get(StartPrefViewModel::class.java)

        binding.btnEn.setOnClickListener {
            Constant.myPref.appLanguage="en"
        }
        binding.btnAr.setOnClickListener {
            Constant.myPref.appLanguage="ar"
        }
        binding.btnGps.setOnClickListener {
            if (!NetworkManager.isInternetConnected()){
                Snackbar.make(view, R.string.internetDisconnected,
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()
                binding.btnGps.isChecked=false
            }
            else {
             /*   viewModel.location.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    myLocation=it
                })*/
             val myGps=  GPSProvider(requireContext())
                myGps.getCurrentLocation()
                myGps.data.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    myLocation=it
                })

            }
        }

        binding.btnMap.setOnClickListener {
            if (!NetworkManager.isInternetConnected()){
                Snackbar.make(view, R.string.internetDisconnected,
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()
                binding.btnMap.isChecked=false
            }
            else {
                val action =
                    StartPrefFragmentDirections.actionStartPrefFragmentToMapsFragment("start")
                findNavController().navigate(action)
            }

        }
        val args by navArgs<StartPrefFragmentArgs>()
        if (args.myLoc !=null){
           myLocation= args.myLoc
            binding.btnMap.isChecked=true
        }



       binding.btnSavePreferences.setOnClickListener {
           if (binding.radioGroupLang.getCheckedRadioButtonId() == -1 || binding.radioGroupLoc.getCheckedRadioButtonId()==-1)
           {
               Snackbar.make(view, R.string.select_all,
                   Snackbar.LENGTH_LONG).setAction("Action", null).show()
           }
           else {
               Constant.myPref.myLocation = myLocation!!
               Preferences.saveMyPref(Constant.myPref,requireContext())
               findNavController().navigate(R.id.action_startPrefFragment_to_homeFragment)
               onBoardingFinished()

           }
        }


        return view
    }
    private fun onBoardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("finished",true)
        editor.apply()
    }



}