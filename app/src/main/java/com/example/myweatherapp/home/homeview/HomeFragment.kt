package com.example.myweatherapp.home.homeview

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentHomeBinding
import com.example.myweatherapp.datasource.Repository
import com.example.myweatherapp.datasource.db.ConcreteLocalSource
import com.example.myweatherapp.datasource.db.LocalSource
import com.example.myweatherapp.datasource.network.ApiClient
import com.example.myweatherapp.home.homeviewmodel.HomeViewModel
import com.example.myweatherapp.home.homeviewmodel.HomeViewModelFactory
import com.example.myweatherapp.utils.loadImage
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import kotlin.math.ceil


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    lateinit var viewModel:HomeViewModel
    lateinit var factory: HomeViewModelFactory
    var localSource = ConcreteLocalSource()


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
        val view = binding.root

        factory = HomeViewModelFactory(
            Repository.getInstance(ApiClient.getInstance(), localSource),

        )




        viewModel = ViewModelProvider(this,factory).get(HomeViewModel::class.java)
        viewModel.weather.observe(viewLifecycleOwner, Observer {
          val  timeAdapter= TimeAdapter(it.current.dt)
          val  dayAdapter= DaysAdapter(it.current.dt)
           dayAdapter.submitList(it.daily)
           timeAdapter.submitList(it.hourly)
            var simpleDate = SimpleDateFormat("dd/M/yyyy")
            var currentDate = simpleDate.format(it.current.dt*1000L)

            binding.textLocation.text=it.timezone
            binding.textDate.text=currentDate.toString()
            loadImage(binding.imageDesc, it.current.weather[0].icon )
            binding.textTempNum.text= ceil(it.current.temp).toInt().toString()
            binding.textTempUnits.text= "°C"   //if Units – default: kelvin, metric: Celsius, imperial: Fahrenheit.
            binding.textDesc.text=it.current.weather[0].description


            binding.pressure.text="${it.current.pressure} hPa"
            binding.humadity.text="${it.current.humidity}%"
            binding.wind.text="${it.current.wind_speed} metre/sec" //if Units – default: metre/sec, metric: metre/sec, imperial: miles/hour.
            binding.cloud.text="${it.current.clouds}%"
            binding.violet.text="${it.current.uvi} UV"
            binding.visibility.text="${it.current.visibility} km"

            binding.daysRecycler.adapter=dayAdapter
            binding.daysRecycler.layoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL,false)
            binding.timeRecycler.adapter=timeAdapter
            binding.timeRecycler.layoutManager= LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)
        })






        return view
    }



}