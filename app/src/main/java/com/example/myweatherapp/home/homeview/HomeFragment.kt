package com.example.myweatherapp.home.homeview

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentHomeBinding
import com.example.myweatherapp.favourite.favView.FavoriteFragmentArgs
import com.example.myweatherapp.home.homeviewmodel.HomeViewModel
import com.example.myweatherapp.home.homeviewmodel.HomeViewModelFactory
import com.example.myweatherapp.location.GPSProvider
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.utils.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
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
        binding.lifecycleOwner=this.viewLifecycleOwner
        val view = binding.root

        Constant.myPref=Preferences.getMyPref(requireContext())
        myLocation = Constant.myPref.myLocation
        if(!Permissions.checkPremission(requireContext())){
            hideUI()
        }
        else {
            setUiData()
        }
        binding.txtGps.setOnClickListener {
            val myGps=  GPSProvider(requireContext())
            myGps.getCurrentLocation()
            myGps.data.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                myLocation=it
                Constant.myPref.myLocation = myLocation
                Preferences.saveMyPref(Constant.myPref,requireContext())
                setUiData()
                if(!Permissions.checkPremission(requireContext())) {
                    Snackbar.make(
                        binding.root, R.string.denied_prem,
                        Snackbar.LENGTH_LONG
                    ).setAction("Action", null).show()
                }
            })


        }
        return view
    }

    private fun setUiData() {
        showUI()
        factory = HomeViewModelFactory(
            MyApp.getInstanceRepository(),
            myLocation
        )
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        if (!NetworkManager.isInternetConnected()) {
            viewModel.getCurrentWeatherFromDB()
/*            Snackbar.make(
                binding.root, R.string.internetDisconnectedFav,
                Snackbar.LENGTH_LONG
            ).setAction("Action", null).show()*/
        }
        viewModel.weather.observe(viewLifecycleOwner, Observer {
            viewModel.addWeather(it)
            val timeAdapter = TimeAdapter(it.current.dt)
            val dayAdapter = DaysAdapter(it.current.dt)
            dayAdapter.submitList(it.daily)
            timeAdapter.submitList(it.hourly)
            var simpleDate = SimpleDateFormat("dd/M/yyyy")
            var currentDate = simpleDate.format(it.current.dt * 1000L)
           // val geocoder = Geocoder(requireContext())
            //val address = geocoder.getFromLocation(it.lat, it.lon, 1)
           // if (address != null && address.isNotEmpty()) {
             //   binding.textLocation.text =
                //    address[0].adminArea + " - " + address[0].countryName
          //  } else {
                binding.textLocation.text = it.timezone
          //  }
            binding.textLocation.text = it.timezone
            binding.textDate.text = currentDate.toString()
            loadImage(binding.imageDesc, it.current.weather[0].icon)
            binding.textTempNum.text = ceil(it.current.temp).toInt().toString()
            binding.textTempUnits.text =
                "°C"   //if Units – default: kelvin, metric: Celsius, imperial: Fahrenheit.
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
        })
    }
    private fun hideUI(){
    binding.txtGps.visibility=View.VISIBLE
    binding.animationView.visibility=View.VISIBLE
    binding.cardMain.visibility=View.GONE
    binding.textLocation.visibility=View.GONE
    binding.textDate.visibility=View.GONE
    binding.scrollView.visibility=View.GONE
}
    private fun showUI(){
        binding.cardMain.visibility=View.VISIBLE
        binding.textLocation.visibility=View.VISIBLE
        binding.textDate.visibility=View.VISIBLE
        binding.scrollView.visibility=View.VISIBLE
        binding.txtGps.visibility=View.GONE
        binding.animationView.visibility=View.GONE
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findNavController().navigate(R.id.action_homeFragment_self)
            } else {
                Snackbar.make(
                    binding.root, R.string.denied_prem,
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null).show()


            }
        }
    }


}