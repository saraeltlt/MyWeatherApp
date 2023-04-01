package com.example.myweatherapp

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.databinding.FragmentDetailsBinding
import com.example.myweatherapp.databinding.FragmentHomeBinding
import com.example.myweatherapp.databinding.FragmentSettingBinding
import com.example.myweatherapp.home.homeview.DaysAdapter
import com.example.myweatherapp.home.homeview.TimeAdapter
import com.example.myweatherapp.home.homeviewmodel.HomeViewModel
import com.example.myweatherapp.home.homeviewmodel.HomeViewModelFactory
import com.example.myweatherapp.model.ApiState
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.utils.Constant
import com.example.myweatherapp.utils.MyApp
import com.example.myweatherapp.utils.NetworkManager
import com.example.myweatherapp.utils.loadImage
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
   companion object{
       var saveFavLocation:LatLng= LatLng(0.0,0.0)
   }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_details,container,false) as FragmentDetailsBinding
        binding.lifecycleOwner=this
        val view = binding.root
        val args by navArgs<DetailsFragmentArgs>()

        if (!NetworkManager.isInternetConnected()){
            setUI(args.currentWeather)
            Snackbar.make(view, R.string.internetDisconnectedFav,
                Snackbar.LENGTH_LONG).setAction("Action", null).show()

        }
        else{
            var factory = HomeViewModelFactory(
                MyApp.getInstanceRepository(),
                LatLng(args.currentWeather.lat,args.currentWeather.lon)
            )
            var viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
            lifecycleScope.launch {
                viewModel.stateFlow.collectLatest {result ->
                    when (result) {
                        is ApiState.Loading -> {
                            hideUI()
                            binding.progressLayout.visibility = View.VISIBLE
                            binding.obaqueBG2.visibility=View.VISIBLE

                        }
                        is ApiState.Failure -> {
                            Toast.makeText(requireContext(),getString(R.string.wentWrong), Toast.LENGTH_LONG).show()
                        }
                        is ApiState.Succcess -> {
                            hideUI()
                            viewModel.addWeather(result.data)
                            setUI(result.data)
                        }
                    }
                }
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing=false
            val action = DetailsFragmentDirections.actionDetailsFragmentSelf(args.currentWeather)
            findNavController().navigate(action)
        }

        return view
    }

@SuppressLint("SetTextI18n")
fun setUI(it:Forecast){
    showUI()
    val timeAdapter = TimeAdapter(it.current.dt)
    val dayAdapter = DaysAdapter(it.current.dt)
    dayAdapter.submitList(it.daily)
    timeAdapter.submitList(it.hourly)
    var simpleDate = SimpleDateFormat("dd/M/yyyy")
    var currentDate = simpleDate.format(it.current.dt * 1000L)
    val geocoder = Geocoder(requireContext(), Locale.forLanguageTag(Constant.myPref.appLanguage))
    val addressList = try {
        geocoder.getFromLocation(it.lat, it.lon, 1)
    } catch (e: IOException) {
        null
    }

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
    binding.textDate.text = currentDate.toString()
    loadImage(binding.imageDesc, it.current.weather[0].icon)
    binding.textTempNum.text = ceil(it.current.temp).toInt().toString()
    binding.textTempUnits.text = "°C"   //if Units – default: kelvin, metric: Celsius, imperial: Fahrenheit.
    binding.textDesc.text = it.current.weather[0].description
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
    private fun hideUI(){


        binding.cardMain.visibility=View.GONE
        binding.textLocation.visibility=View.GONE
        binding.textDate.visibility=View.GONE
        binding.scrollView.visibility=View.GONE
        binding.progressLayout.visibility = View.GONE
        binding.obaqueBG2.visibility=View.GONE
    }
    private fun showUI(){
        binding.cardMain.visibility=View.VISIBLE
        binding.textLocation.visibility=View.VISIBLE
        binding.textDate.visibility=View.VISIBLE
        binding.scrollView.visibility=View.VISIBLE
        binding.progressLayout.visibility = View.GONE
        binding.obaqueBG2.visibility=View.GONE
    }
}