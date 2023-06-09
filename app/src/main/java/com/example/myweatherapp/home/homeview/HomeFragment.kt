package com.example.myweatherapp.home.homeview

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.RemoteException
import android.provider.Settings
import android.provider.SyncStateContract.Constants
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentHomeBinding
import com.example.myweatherapp.home.homeviewmodel.HomeViewModel
import com.example.myweatherapp.home.homeviewmodel.HomeViewModelFactory
import com.example.myweatherapp.location.GPSProvider
import com.example.myweatherapp.model.ApiState
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.utils.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    lateinit var viewModel:HomeViewModel
    lateinit var factory: HomeViewModelFactory
    lateinit var myLocation : LatLng





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNav=requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.visibility= View.VISIBLE
        val viewLine=requireActivity().findViewById<View>(R.id.viewLine)
        viewLine.visibility= View.VISIBLE



    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false) as FragmentHomeBinding
        binding.lifecycleOwner=this
        var view = binding.root


        binding.swipeRefreshLayout.setOnRefreshListener {
          binding.swipeRefreshLayout.isRefreshing=false
            findNavController().navigate(R.id.action_homeFragment_self)
        }



        Constant.myPref=Preferences.getMyPref(requireContext())
        myLocation = Constant.myPref.myLocation
        if(!Permissions.checkPremission(requireContext()) /*|| myLocation==LatLng(0.0,0.0)*/){
            hideUI()
            binding.txtGps.visibility=View.VISIBLE
            binding.animationView.visibility=View.VISIBLE
        }
        else {
            hideUI()
           getData(view)
        }


        binding.txtGps.setOnClickListener {
            requestPermission()

        }

        return view
    }
    private fun getData(view:View){
        factory = HomeViewModelFactory(
            MyApp.getInstanceRepository(),
            myLocation
        )
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)


        if (!NetworkManager.isInternetConnected()) {
            viewModel.getCurrentWeatherFromDB()
            viewModel.weather.observe(viewLifecycleOwner, Observer {
               if (it==null){ //empty room
                    hideUI()
                    binding.animationViewWifi.visibility=View.VISIBLE

                }
                else {
                    setUiData(it)
                   val snackbar = Snackbar.make(binding.root, R.string.internetDisconnectedFav, Snackbar.LENGTH_LONG)
                   snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_toast))
                   snackbar.setTextColor(Color.WHITE)
                   snackbar.show()
               }
            })



        }
        else{
            lifecycleScope.launch {
                viewModel.stateFlow.collectLatest {result ->
                    when (result) {
                        is ApiState.Loading -> {
                            hideUI()
                            binding.progressLayout.visibility = View.VISIBLE
                            binding.obaqueBG.visibility=View.VISIBLE

                        }
                        is ApiState.Failure -> {
                            Log.e("eh ba2a", result.msg.toString())
                            Toast.makeText(requireContext(),getString(R.string.wentWrong),Toast.LENGTH_LONG).show()
                        }
                        is ApiState.Succcess -> {
                            hideUI()
                            viewModel.addWeather(result.data)
                            setUiData(result.data)
                        }
                    }
                }
            }

        }

    }

    @SuppressLint("SetTextI18n")
    private fun setUiData(it: Forecast) {
        showUI()
            val timeAdapter = TimeAdapter(it.current.dt)
            val dayAdapter = DaysAdapter(it.current.dt)
            dayAdapter.submitList(it.daily)
            timeAdapter.submitList(it.hourly)
            var simpleDate = SimpleDateFormat("dd/M/yyyy")
            var currentDate = simpleDate.format(it.current.dt * 1000L)
            val geocoder = Geocoder(requireContext(), Locale.forLanguageTag(Constant.myPref.appLanguage))
           try {
               val addressList =  geocoder.getFromLocation(it.lat, it.lon, 1)
               if (addressList == null || addressList.isEmpty()) {
                   binding.textLocation.text = it.timezone
               } else {
                   val address = addressList[0]
                   if (address.subAdminArea!=null){
                       binding.textLocation.text = address.subAdminArea+ " - " +
                               address.adminArea + " - " + address.countryName
                   }
                   else {
                       binding.textLocation.text =
                           address.adminArea + " - " + address.countryName
                   }
               }
        } catch (e: IOException ) {
               binding.textLocation.text = it.timezone

        }catch (e: RemoteException) {
               binding.textLocation.text = it.timezone
           }



            binding.textDate.text = currentDate.toString()
            //loadImage(binding.imageDesc, it.current.weather[0].icon)
        binding.imageDesc.setImageResource(MyIcons.mapIcon[it.current.weather[0].icon]!!)
            binding.textTempNum.text = ceil(it.current.temp).toInt().toString()

        //units
             if(Constant.myPref.appUnit=="metric") {
                 binding.textTempUnits.text=getString(R.string.c)
                 binding.wind.text =  "${it.current.wind_speed}  ${getString(R.string.metreـsec)}"
             }else if(Constant.myPref.appUnit=="standard"){
                 binding.textTempUnits.text = getString(R.string.k)
                 binding.wind.text =
                     "${it.current.wind_speed}  ${getString(R.string.metreـsec)}"
             }else if(Constant.myPref.appUnit=="imperial"){
                 binding.textTempUnits.text = getString(R.string.f)
                 binding.wind.text =
                     "${it.current.wind_speed}  ${getString(R.string.milesـhour)}"
             }


            binding.textDesc.text = it.current.weather[0].description

            binding.pressure.text = "${it.current.pressure} ${getString(R.string.hpa)}"
            binding.humadity.text = "${it.current.humidity} %"
            binding.cloud.text = "${it.current.clouds} %"
            binding.violet.text = "${it.current.uvi}  ${getString(R.string.uv)}"
            binding.visibility.text = "${it.current.visibility}  ${getString(R.string.km)}"

            binding.daysRecycler.adapter = dayAdapter
            binding.daysRecycler.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.timeRecycler.adapter = timeAdapter
            binding.timeRecycler.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }
    private fun hideUI(){ binding.animationViewWifi.visibility=View.GONE
    binding.txtGps.visibility=View.GONE
    binding.animationView.visibility=View.GONE
    binding.cardMain.visibility=View.GONE
    binding.textLocation.visibility=View.GONE
    binding.textDate.visibility=View.GONE
    binding.scrollView.visibility=View.GONE
        binding.progressLayout.visibility = View.GONE
        binding.obaqueBG.visibility=View.GONE
}
    private fun showUI(){
        binding.cardMain.visibility=View.VISIBLE
        binding.textLocation.visibility=View.VISIBLE
        binding.textDate.visibility=View.VISIBLE
        binding.scrollView.visibility=View.VISIBLE
        binding.txtGps.visibility=View.GONE
        binding.animationView.visibility=View.GONE
        binding.progressLayout.visibility = View.GONE
        binding.obaqueBG.visibility=View.GONE
    }
   override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("hena","wala mesh hena?")
                val myGps = GPSProvider(requireContext())
                myGps.getCurrentLocations()
                myGps.data.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    myLocation = it
                })
                Constant.myPref.myLocation = myLocation
                Preferences.saveMyPref(Constant.myPref, requireContext())
                hideUI()
                getData(binding.root)
            }  else if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION) ) {
                hideUI()
                binding.txtGps.visibility=View.VISIBLE
                binding.animationView.visibility=View.VISIBLE

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