package com.example.myweatherapp.home.homeview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentHomeBinding
import com.example.myweatherapp.datasource.Repository
import com.example.myweatherapp.datasource.db.ConcreteLocalSource
import com.example.myweatherapp.datasource.db.LocalSource
import com.example.myweatherapp.datasource.network.ApiClient
import com.example.myweatherapp.home.homeviewmodel.HomeViewModel
import com.example.myweatherapp.home.homeviewmodel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false) as FragmentHomeBinding
        binding.lifecycleOwner=this
        val view = binding.root

      /*  factory = HomeViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
                localSource
            )
        )

        viewModel = ViewModelProvider(this,factory).get(HomeViewModel::class.java)
        viewModel.weather.observe(viewLifecycleOwner, Observer {
           Log.d("yarab", it.timezone)
        })*/







        return view
    }


}