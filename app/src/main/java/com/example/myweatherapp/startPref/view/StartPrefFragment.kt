package com.example.myweatherapp.startPref.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.example.myweatherapp.utils.Permissions
import com.example.myweatherapp.utils.Preferences
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.delay
import java.util.*


class StartPrefFragment : Fragment() {
    private lateinit var binding: FragmentStartPrefBinding
    private var myLocation: LatLng? = null
    var flagFrom :Boolean=false
    val maxRequests = 2
    var requestCount = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNav=requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.visibility= View.GONE
        val viewLine=requireActivity().findViewById<View>(R.id.viewLine)
        viewLine.visibility= View.GONE

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_start_pref,container,false) as FragmentStartPrefBinding
        binding.lifecycleOwner=this
        val view = binding.root
        binding.progressLayout.visibility= View.GONE
        binding.obaqueBG.visibility= View.GONE
        val factory = StartPrefViewModelFactory(requireContext(),  GPSProvider(requireContext()))
        val viewModel =
            ViewModelProvider(this, factory).get(StartPrefViewModel::class.java)
        requestPermission()

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
                if(Permissions.checkPremission(requireContext())) {
                    val myGps = GPSProvider(requireContext())
                    myGps.getCurrentLocations()
                    myGps.data.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                        myLocation = it

                    })
                    binding.progressLayout.visibility = View.VISIBLE
                    binding.obaqueBG.visibility = View.VISIBLE
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        binding.progressLayout.visibility = View.GONE
                        binding.obaqueBG.visibility = View.GONE
                    }, 2300)


                }else{
                    flagFrom=true
                    requestPermission()
                }
            }
        }

        binding.btnMap.setOnClickListener {
            if (!NetworkManager.isInternetConnected()){
                Snackbar.make(view, R.string.internetDisconnected,
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()
                binding.btnMap.isChecked=false
            }
            else {
                if (!Permissions.checkPremission(requireContext())) {
                    flagFrom=false
                    requestPermission()
                    /*Snackbar.make(
                        binding.root, R.string.denied_prem,
                        Snackbar.LENGTH_LONG
                    ).setAction("Action", null).show()*/
                }
                else {
                    val action =
                        StartPrefFragmentDirections.actionStartPrefFragmentToMapsFragment("start")
                    findNavController().navigate(action)
                }

            }

        }
        val args by navArgs<StartPrefFragmentArgs>()
        if (args.myLoc !=null){
           myLocation= args.myLoc
            binding.btnMap.isChecked=true

            //language
            if ( Constant.myPref.appLanguage=="ar"){
                binding.btnAr.isChecked=true
            }else if (Constant.myPref.appLanguage=="en"){
                binding.btnEn.isChecked=true
            }
        }



       binding.btnSavePreferences.setOnClickListener {
           if (binding.radioGroupLang.getCheckedRadioButtonId() == -1 || binding.radioGroupLoc.getCheckedRadioButtonId()==-1)
           {
               Snackbar.make(view, R.string.select_all,
                   Snackbar.LENGTH_LONG).setAction("Action", null).show()
           }
           else {
               Constant.myPref.myLocation = myLocation ?: LatLng(0.0,0.0)
               Preferences.saveMyPref(Constant.myPref,requireContext())
               Preferences.setLocale(Constant.myPref.appLanguage, requireContext())
               findNavController().navigate(R.id.action_startPrefFragment_to_homeFragment)
               onBoardingFinished()

           }
        }


        return view
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
        ), Constant.LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun onBoardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("finished",true)
        editor.apply()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constant.LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if(flagFrom){
                    val myGps = GPSProvider(requireContext())
                    myGps.getCurrentLocations()
                    myGps.data.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                        myLocation = it

                    })
                    binding.progressLayout.visibility = View.VISIBLE
                    binding.obaqueBG.visibility = View.VISIBLE
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        binding.progressLayout.visibility = View.GONE
                        binding.obaqueBG.visibility = View.GONE
                    }, 2300)
                }else {
                    val action =
                        StartPrefFragmentDirections.actionStartPrefFragmentToMapsFragment("start")
                    findNavController().navigate(action)
                }
            }

            else if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION) ) {

            }
            else{
                val snackbar = Snackbar.make(binding.root, R.string.denied_prem, Snackbar.LENGTH_LONG)
                snackbar.setAction(R.string.openSittings) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.fromParts("package", requireContext().packageName, null)
                    requireContext().startActivity(intent)
                }
                snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_navy))
                snackbar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.dark_orange))
                snackbar.setTextColor(Color.WHITE)
                snackbar.show()

            }


        }

    }
}


