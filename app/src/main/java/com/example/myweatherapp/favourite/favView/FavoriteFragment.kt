package com.example.myweatherapp.favourite.favView

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentFavoriteBinding
import com.example.myweatherapp.favourite.favViewModel.FavViewModel
import com.example.myweatherapp.favourite.favViewModel.FavViewModelFactory
import com.example.myweatherapp.model.ApiState
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.ui.ConfirmDeleteInterface
import com.example.myweatherapp.ui.ConfirmDialogFragment
import com.example.myweatherapp.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment(),OnFavClickListner, ConfirmDeleteInterface {
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
        binding.progressLayout.visibility = View.GONE
        binding.obaqueBG.visibility=View.GONE
    }
    companion object{
        var forecastRemove:Forecast?=null
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
        if (!NetworkManager.isInternetConnected()){
            val snackbar = Snackbar.make(binding.root, R.string.internetDisconnected, Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_toast))
            snackbar.setTextColor(Color.WHITE)
            snackbar.show()

        }
        else{
            if (!Permissions.checkPremission(requireContext())) {
               requestPermission()
            }else {
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToMapsFragment("fav")
                findNavController().navigate(action)
            }

        }

        }

        return view
    }

    override fun onFavClick(forecast: Forecast) {
        var favItem = forecast
        if (NetworkManager.isInternetConnected()){
            viewModel.getFavRemote(forecast.lat,forecast.lon)
            lifecycleScope.launch {
                viewModel.stateFlow.collectLatest { result ->
                    when (result) {
                        is ApiState.Loading -> {
                            binding.progressLayout.visibility = View.VISIBLE
                            binding.obaqueBG.visibility=View.VISIBLE

                        }
                        is ApiState.Failure -> {
                            favItem=forecast
                        }
                        is ApiState.Succcess -> {
                            favItem=result.data


                        }
                    }
                }
            }
        }

        val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(favItem)
        findNavController().navigate(action)


    }

    override fun onDeleteClick(forecast: Forecast) {
        forecastRemove=forecast
        var dialog= ConfirmDialogFragment(this)
        dialog.show(childFragmentManager, "alertDialog")



    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToMapsFragment("fav")
                findNavController().navigate(action)
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION) ) {

            }
            else{
                val snackbar = Snackbar.make(binding.root, R.string.denied_prem, Snackbar.LENGTH_LONG)
                snackbar.setAction(R.string.openSittings) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.fromParts("package", requireContext().packageName, null)
                    requireContext().startActivity(intent)
                }
                snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_toast))
                snackbar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.dark_orange))
                snackbar.setTextColor(Color.WHITE)
                snackbar.show()

            }
        }
    }

    override fun onClick(confirmDelete: Boolean) {
        if (confirmDelete){
            viewModel.deleteFav(forecastRemove!!)
            Toast.makeText(requireContext(),R.string.removedFromFavorites,Toast.LENGTH_LONG).show()
        }
    }
    private fun requestPermission() {
        requestPermissions(arrayOf(
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
        ), Constant.LOCATION_PERMISSION_REQUEST_CODE)
    }


}


