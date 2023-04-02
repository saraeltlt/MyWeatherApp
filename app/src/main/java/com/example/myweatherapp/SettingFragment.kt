package com.example.myweatherapp

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myweatherapp.databinding.FragmentNotificationBinding
import com.example.myweatherapp.databinding.FragmentSettingBinding
import com.example.myweatherapp.location.GPSProvider
import com.example.myweatherapp.startPref.view.StartPrefFragmentArgs
import com.example.myweatherapp.startPref.view.StartPrefFragmentDirections
import com.example.myweatherapp.utils.Constant
import com.example.myweatherapp.utils.NetworkManager
import com.example.myweatherapp.utils.Permissions
import com.example.myweatherapp.utils.Preferences
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    var flagFrom :Boolean=false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNav=requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.visibility= View.VISIBLE
        val viewLine=requireActivity().findViewById<View>(R.id.viewLine)
        viewLine.visibility= View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = DataBindingUtil.inflate(inflater,R.layout.fragment_setting,container,false) as FragmentSettingBinding
        binding.lifecycleOwner=this
        binding.progressLayout.visibility= View.GONE
        binding.obaqueBG.visibility= View.GONE
        initialChecks()
        //language
        binding.btnAr.setOnClickListener {
            Constant.myPref.appLanguage="ar"
            Preferences.setMood("ar")
        }
        binding.btnEn.setOnClickListener {
            Constant.myPref.appLanguage="en"
            Preferences.setMood("en")
        }
        //mood
        binding.btnLight.setOnClickListener {
            Constant.myPref.appMode="light"
               Preferences.setMood("light")

        }
        binding.btnDark.setOnClickListener {
            Constant.myPref.appMode="dark"
            Preferences.setMood("dark")

        }
        //unites
        binding.btnMetric.setOnClickListener {
            Constant.myPref.appUnit="metric"
            Snackbar.make(binding.root, "${requireContext().getResources().getString(R.string.changeUnit)} \n ${requireContext().getResources().getString(R.string.metric)} "  ,
                Snackbar.LENGTH_LONG).setAction("Action", null).show()

        }
        binding.btnStandard.setOnClickListener {
            Constant.myPref.appUnit="standard"
            Snackbar.make(binding.root, "${requireContext().getResources().getString(R.string.changeUnit)} \n ${requireContext().getResources().getString(R.string.standard)}"  ,
                Snackbar.LENGTH_LONG).setAction("Action", null).show()


        }
       binding.btnImperia.setOnClickListener {
            Constant.myPref.appUnit="imperial"
           Snackbar.make(binding.root, "${requireContext().getResources().getString(R.string.changeUnit)} \n ${requireContext().getResources().getString(R.string.imperial)} "  ,
               Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }

        //location
         binding.btnGps.setOnClickListener {
            if (!NetworkManager.isInternetConnected()){
                val snackbar = Snackbar.make(binding.root, R.string.internetDisconnectedFav, Snackbar.LENGTH_INDEFINITE)
                snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_navy))
                snackbar.setTextColor(Color.WHITE)
                snackbar.show()
                binding.btnGps.isChecked=false
            }
            else {

                if(Permissions.checkPremission(requireContext())) {
                    val myGps = GPSProvider(requireContext())
                    myGps.getCurrentLocations()
                    myGps.data.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                        Constant.myPref.myLocation = it

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
                val snackbar = Snackbar.make(binding.root, R.string.internetDisconnectedFav, Snackbar.LENGTH_INDEFINITE)
                snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_navy))
                snackbar.setTextColor(Color.WHITE)
                snackbar.show()
                binding.btnMap.isChecked=false
            }
            else {
                if (!Permissions.checkPremission(requireContext())) {
                    flagFrom=false
                    requestPermission()
                }
                else {
                    val action =SettingFragmentDirections.actionSettingFragmentToMapsFragment("sittings")
                    findNavController().navigate(action)
                }

            }
        }
        val args by navArgs<SettingFragmentArgs>()
        if (args.myLoc !=null){
            Constant.myPref.myLocation= args.myLoc!!
            binding.btnMap.isChecked=true
            initialChecks()
        }





        return binding.root
    }


    fun initialChecks(){

        //language
        if ( Constant.myPref.appLanguage=="ar"){
            binding.btnAr.isChecked=true
        }else if (Constant.myPref.appLanguage=="en"){
            binding.btnEn.isChecked=true
        }

        //mood
        if ( Constant.myPref.appMode=="light"){
            binding.btnLight.isChecked=true
        }else if (Constant.myPref.appMode=="dark"){
            binding.btnDark.isChecked=true
        }

        //unites
        if (Constant.myPref.appUnit=="metric"){
            binding.btnMetric.isChecked=true
        }else if (Constant.myPref.appUnit=="standard"){
            binding.btnStandard.isChecked=true
        }else if(Constant.myPref.appUnit=="imperial"){
            binding.btnImperia.isChecked=true
        }
        //Location
        //no init check user determin location type on click

    }


    //if pressed back button
    override fun onDestroy() {
        super.onDestroy()
        Preferences.saveMyPref(Constant.myPref,requireContext())
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Preferences.saveMyPref(Constant.myPref,requireContext())
    }

    //if Pressed on another farg
    override fun onPause() {
        super.onPause()
        Preferences.saveMyPref(Constant.myPref,requireContext())
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(flagFrom){
                    val myGps = GPSProvider(requireContext())
                    myGps.getCurrentLocations()
                    myGps.data.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                        Constant.myPref.myLocation  = it

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
            }  else if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION) ) {

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
    private fun requestPermission() {
        requestPermissions(arrayOf(
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
        ), Constant.LOCATION_PERMISSION_REQUEST_CODE)
    }

}