package com.example.myweatherapp.map

import android.app.Activity
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentMapsBinding
import com.example.myweatherapp.utils.Constant
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback


class MapsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapsBinding
    private var mMap :GoogleMap? = null
    private val MAP_VIEW_BUNDLE_KEY = "MaoViewBundleKey"



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_maps,container,false) as FragmentMapsBinding
        binding.lifecycleOwner=this
        askGallaryPermissionLocation()
        var mapViewBundle:Bundle?=null
        if (savedInstanceState!=null){
            mapViewBundle=savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }
        binding.map.onCreate(mapViewBundle)
        binding.map.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        binding.map.onResume()
        mMap=googleMap
        checkPremission()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        askGallaryPermissionLocation()

        var  mapViewBundle=outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle==null){
            mapViewBundle=Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }
        binding.map.onSaveInstanceState(mapViewBundle)

    }

    private fun askGallaryPermissionLocation() {

            ActivityCompat.requestPermissions(context as Activity, arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), Constant.LOCATION_PERMISSION_REQUEST_CODE)

    }
    private fun checkPremission(){
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED){
            return
        }
        mMap!!.setMyLocationEnabled(true)
    }


}