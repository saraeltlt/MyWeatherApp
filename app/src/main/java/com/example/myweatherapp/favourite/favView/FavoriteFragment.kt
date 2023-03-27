package com.example.myweatherapp.favourite.favView

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavAction
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.DetailsFragment
import com.example.myweatherapp.utils.MyApp
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentFavoriteBinding
import com.example.myweatherapp.favourite.favViewModel.FavViewModel
import com.example.myweatherapp.favourite.favViewModel.FavViewModelFactory
import com.example.myweatherapp.location.MapsFragmentArgs
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.startPref.view.StartPrefFragmentDirections
import com.example.myweatherapp.utils.Constant
import com.example.myweatherapp.utils.NetworkManager
import com.example.myweatherapp.utils.Permissions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment(),OnFavClickListner {
    private lateinit var binding:FragmentFavoriteBinding
    lateinit var viewModel: FavViewModel
    lateinit var factory: FavViewModelFactory
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
           MyApp.getInstanceRepository()
        )
        adapter= FavAdapter(this)
        binding.favRecycler.adapter=adapter
        binding.favRecycler.layoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        viewModel = ViewModelProvider(this,factory).get(FavViewModel::class.java)
        val args by navArgs<FavoriteFragmentArgs>()
        if (args.myLocation !=null){
            viewModel.getFavRemote(args.myLocation?.latitude!!, args.myLocation?.longitude!!)
        }
        viewModel.favList.observe(viewLifecycleOwner, Observer {

                if (it.isEmpty()){
                    binding.favRecycler.visibility= View.GONE
                    binding.animationView.visibility= View.VISIBLE
                    binding.txtFav.visibility=View.VISIBLE

                }
                else {
                    binding.animationView.visibility= View.GONE
                    binding.txtFav.visibility=View.GONE
                    binding.favRecycler.visibility= View.VISIBLE
                    adapter.submitList(it)
                }
        })
        binding.favFab.setOnClickListener {
            if (!Permissions.checkPremission(requireContext())) {
                Permissions.requestPermission(requireContext())
                Snackbar.make(
                    binding.root, R.string.denied_prem,
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null).show()
            }
            else {
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToMapsFragment("fav")
                findNavController().navigate(action)
            }


        }

        return view
    }

    override fun onFavClick(forecast: Forecast) {
        var favItem=forecast
        if (NetworkManager.isInternetConnected()){
            viewModel.getFavRemote(forecast.lat,forecast.lon)
            favItem=viewModel.favItem
        }
       val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(favItem)
        findNavController().navigate(action)




    }

    override fun onDeleteClick(forecast: Forecast) {
      viewModel.deleteFav(forecast)
        Toast.makeText(requireContext(),R.string.removedFromFavorites,Toast.LENGTH_LONG).show()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToMapsFragment("fav")
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Location permission denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


