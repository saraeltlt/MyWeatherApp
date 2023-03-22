package com.example.myweatherapp.favourite.favView

import android.os.Bundle
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
import com.example.myweatherapp.databinding.FragmentFavoriteBinding
import com.example.myweatherapp.datasource.Repository
import com.example.myweatherapp.datasource.db.ConcreteLocalSource
import com.example.myweatherapp.datasource.network.ApiClient
import com.example.myweatherapp.favourite.favViewModel.FavViewModel
import com.example.myweatherapp.favourite.favViewModel.FavViewModelFactory
import com.example.myweatherapp.home.homeviewmodel.HomeViewModel
import com.example.myweatherapp.home.homeviewmodel.HomeViewModelFactory
import com.example.myweatherapp.model.Forecast
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoriteFragment : Fragment(),OnFavClickListner {
    private lateinit var binding:FragmentFavoriteBinding
    lateinit var viewModel: FavViewModel
    lateinit var factory: FavViewModelFactory
    var localSource = ConcreteLocalSource()
    lateinit var adapter: FavAdapter
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
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite,container,false) as FragmentFavoriteBinding
        binding.lifecycleOwner=this
        val view = binding.root

        factory = FavViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
                localSource
            )
        )

        adapter= FavAdapter(this)
        binding.favRecycler.adapter=adapter
        binding.favRecycler.layoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL,false)

        viewModel = ViewModelProvider(this,factory).get(FavViewModel::class.java)
       /* viewModel.weather.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        } */
               binding.favFab.setOnClickListener {

        }

        return view
    }

    override fun onFavClick(forecast: Forecast) {
        TODO("Not yet implemented")
    }

    override fun onDeleteClick(forecast: Forecast) {
        TODO("Not yet implemented")
    }


}