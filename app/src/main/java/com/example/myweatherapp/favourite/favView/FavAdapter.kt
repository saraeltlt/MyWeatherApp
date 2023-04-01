package com.example.myweatherapp.favourite.favView

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.CardFavBinding
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.model.ForecastDiffUtil
import com.example.myweatherapp.utils.Constant
import java.io.IOException
import java.util.*

class FavAdapter (
    var action: OnFavClickListner

): ListAdapter<Forecast, FavAdapter.ViewHolder>(ForecastDiffUtil())  {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
        val binding: CardFavBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.card_fav,parent,false)
        return ViewHolder(binding)
    }



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val favItem=getItem(position)
        val geocoder = Geocoder(context, Locale.forLanguageTag(Constant.myPref.appLanguage))
        val addressList = try {
            geocoder.getFromLocation(favItem.lat,favItem.lon,1)
        } catch (e: IOException) {
            null
        }

        if (addressList == null || addressList.isEmpty()) {
            holder.binding.textLocation.text = favItem.timezone
        } else {
            val address = addressList[0]
            holder.binding.textLocation.text =
                address.adminArea + " - " + address.countryName
        }

        holder.binding.forecast=favItem
        holder.binding.action=action
    }

    class ViewHolder(val binding: CardFavBinding) : RecyclerView.ViewHolder(binding.root)

}

interface OnFavClickListner {
    fun onFavClick(forecast: Forecast)
    fun onDeleteClick(forecast: Forecast)
}