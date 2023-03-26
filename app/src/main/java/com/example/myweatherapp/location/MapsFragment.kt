package com.example.myweatherapp.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentMapsBinding
import com.example.myweatherapp.favourite.favView.FavoriteFragmentDirections
import com.example.myweatherapp.utils.Constant
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MapsFragment : Fragment(), OnMapReadyCallback , LocationListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener{
    private lateinit var binding: FragmentMapsBinding
    private var mMap :GoogleMap? = null
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    private val DEFAULT_ZOOM=12f
    private  var fusedClient: FusedLocationProviderClient?=null
    private var destination : String = ""
    private var myLocation = LatLng(0.0,0.0)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MapsInitializer.initialize(requireContext())
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_maps,container,false) as FragmentMapsBinding
        binding.lifecycleOwner=this
        //---------------------FROM--------------------------------
        val args by navArgs<MapsFragmentArgs>()
        destination=args.from
        if (args.from=="fav"){
            binding.button2.setText(getString(R.string.add_fav))
        }else if (args.from=="start") {
            binding.button2.setText(getString(R.string.set_loc))
        }
        //---------------------------------------------------------
        if (!checkPremission()) {
            requestPermission()
        }

            var mapViewBundle: Bundle? = null
            if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
            }
            binding.map.onCreate(mapViewBundle)
            binding.map.getMapAsync(this)

            binding.txtAddress.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.action == KeyEvent.ACTION_DOWN
                    || event.action == KeyEvent.KEYCODE_ENTER
                ) {
                    goToSearchLocation()
                }
                false

            }


            binding.button2.setOnClickListener {
                if (destination == "fav") {
                    val action =
                        MapsFragmentDirections.actionMapsFragmentToFavoriteFragment(myLocation)
                    findNavController().navigate(action)
                } else if (args.from == "start") {
                    val action =
                        MapsFragmentDirections.actionMapsFragmentToStartPrefFragment(myLocation)
                    findNavController().navigate(action)
                }

            }


        return binding.root
    }






    private fun goToSearchLocation() {
        val searchLocation = binding.txtAddress.text.toString()
        val geocoder = Geocoder(requireContext())
        var list : List<Address> = ArrayList()
        try{
            list = geocoder.getFromLocationName(searchLocation,1) as List<Address>
        }catch (e:IOException){
            e.printStackTrace()
        }
        if (list.isNotEmpty()){
            val update = CameraUpdateFactory.newLatLngZoom(LatLng(list[0].latitude,list[0].longitude),DEFAULT_ZOOM)
            mMap?.animateCamera(update)

        }

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var  mapViewBundle=outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle==null){
            mapViewBundle=Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }
        binding.map.onSaveInstanceState(mapViewBundle)

    }
    private fun askPermissionLocation() {

            ActivityCompat.requestPermissions(context as Activity, arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), Constant.LOCATION_PERMISSION_REQUEST_CODE)

    }
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedClient = LocationServices.getFusedLocationProviderClient(requireContext())
        try{
            val location=fusedClient!!.lastLocation
            location.addOnCompleteListener {
                if (it.isSuccessful){
                    val currentlocation=it.result as Location?
                    if (currentlocation!=null){
                        moveCamera(
                            LatLng(currentlocation.latitude,currentlocation.longitude),
                            DEFAULT_ZOOM
                        )
                    }
                }else{
                    askPermissionLocation()
                }
            }

        }catch(e:Exception){
            Log.e("error",e.toString())

        }

    }
    private fun moveCamera(latLng: LatLng, defaultZoom: Float) {
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,defaultZoom))


    }
    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        binding.map.onResume()
        mMap=p0
        mMap!!.setMyLocationEnabled(true)
        mMap!!.setOnCameraMoveListener (this)
        mMap!!.setOnCameraMoveStartedListener(this)
        mMap!!.setOnCameraIdleListener(this)
        getCurrentLocation()
    }
    override fun onCameraIdle() {
         var addresses: List<Address>?=null
        val geocoder=Geocoder(requireContext())
        try{
            addresses=geocoder.getFromLocation(mMap!!.cameraPosition.target.latitude,mMap!!.cameraPosition.target.longitude,1)
            myLocation = LatLng(mMap!!.cameraPosition.target.latitude,mMap!!.cameraPosition.target.longitude)
            Log.e("location",myLocation.latitude.toString())
            binding.txtAddress.getText().clear()
            binding.txtAddress.setHint(addresses?.get(0)?.adminArea + " - " + addresses?.get(0)?.countryName )
        }catch (e:java.lang.IndexOutOfBoundsException){
            e.printStackTrace()
        }catch (e:IOException){
            e.printStackTrace()
        }
    }
    override fun onCameraMove() {
    }
    override fun onCameraMoveStarted(p0: Int) {
    }
    override fun onLocationChanged(location: Location) {
    }
    private fun checkPremission():Boolean{
        val result = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        return result
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(context as Activity, arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ), Constant.LOCATION_PERMISSION_REQUEST_CODE)
    }

}