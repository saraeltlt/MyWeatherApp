package com.example.myweatherapp

import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.databinding.FragmentDetailsBinding
import com.example.myweatherapp.databinding.FragmentHomeBinding
import com.example.myweatherapp.databinding.FragmentSettingBinding
import com.example.myweatherapp.home.homeview.DaysAdapter
import com.example.myweatherapp.home.homeview.TimeAdapter
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.utils.NetworkManager
import com.example.myweatherapp.utils.loadImage
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import kotlin.math.ceil

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_details,container,false) as FragmentDetailsBinding
        binding.lifecycleOwner=this
        val view = binding.root
        if (!NetworkManager.isInternetConnected()){
            Snackbar.make(view, R.string.internetDisconnectedFav,
                Snackbar.LENGTH_LONG).setAction("Action", null).show()

        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing=false
            findNavController().navigate(R.id.action_detailsFragment_self)
        }
        val args by navArgs<DetailsFragmentArgs>()
        setUI(args.currentWeather)
        return view
    }

fun setUI(it:Forecast){
    val timeAdapter = TimeAdapter(it.current.dt)
    val dayAdapter = DaysAdapter(it.current.dt)
    dayAdapter.submitList(it.daily)
    timeAdapter.submitList(it.hourly)
    var simpleDate = SimpleDateFormat("dd/M/yyyy")
    var currentDate = simpleDate.format(it.current.dt * 1000L)

    val geocoder= Geocoder(requireContext())
    val address=geocoder.getFromLocation(it.lat,it.lon,1)
    binding.textLocation.text = address?.get(0)?.adminArea + " - " + address?.get(0)?.countryName
    binding.textDate.text = currentDate.toString()
    loadImage(binding.imageDesc, it.current.weather[0].icon)
    binding.textTempNum.text = ceil(it.current.temp).toInt().toString()
    binding.textTempUnits.text = "°C"   //if Units – default: kelvin, metric: Celsius, imperial: Fahrenheit.
    binding.textDesc.text = it.current.weather[0].description

    binding.pressure.text = "${it.current.pressure} hPa"
    binding.humadity.text = "${it.current.humidity}%"
    binding.wind.text =
        "${it.current.wind_speed} metre/sec" //if Units – default: metre/sec, metric: metre/sec, imperial: miles/hour.
    binding.cloud.text = "${it.current.clouds}%"
    binding.violet.text = "${it.current.uvi} UV"
    binding.visibility.text = "${it.current.visibility} km"

    binding.daysRecycler.adapter = dayAdapter
    binding.daysRecycler.layoutManager =
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    binding.timeRecycler.adapter = timeAdapter
    binding.timeRecycler.layoutManager =
        LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
}
}