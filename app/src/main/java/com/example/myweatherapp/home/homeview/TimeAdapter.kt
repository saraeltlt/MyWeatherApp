package com.example.myweatherapp.home.homeview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.CardTimesBinding

import com.example.myweatherapp.model.Hourly

import com.example.myweatherapp.favourite.favView.HoursDiffUtil
import com.example.myweatherapp.utils.Constant
import java.text.SimpleDateFormat
import java.util.*

class TimeAdapter (
var dtToday:Int

): ListAdapter<Hourly, TimeAdapter.ViewHolder>(HoursDiffUtil())  {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
        val binding: CardTimesBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.card_times,parent,false)
        return ViewHolder(binding)
    }



    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val hour=getItem(position)
        holder.binding.hourly=hour
        //units
        if(Constant.myPref.appUnit=="metric") {
            holder.binding.textTemp.text= "${Math.ceil(hour.temp).toInt()} ${context.getResources().getString(R.string.c)}"
        }else if(Constant.myPref.appUnit=="standard"){
            holder.binding.textTemp.text= "${Math.ceil(hour.temp).toInt()} ${context.getResources().getString(R.string.k)}"
        }else if(Constant.myPref.appUnit=="imperial"){
            holder.binding.textTemp.text= "${Math.ceil(hour.temp).toInt()} ${context.getResources().getString(R.string.f)}"
        }

        holder.binding.textTime.text=getCurrentTime(hour.dt)
    }

    class ViewHolder(val binding: CardTimesBinding) : RecyclerView.ViewHolder(binding.root)
    fun getCurrentTime( dt: Int) : String{
        var date= Date(dt*1000L)
        var sdf= SimpleDateFormat("hh:mm a")
        sdf.timeZone=TimeZone.getDefault()
        return sdf.format(date)
    }

}

