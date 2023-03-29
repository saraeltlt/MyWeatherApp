package com.example.myweatherapp.home.homeview

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.CardDaysBinding

import com.example.myweatherapp.model.Daily
import com.example.myweatherapp.model.DailyDiffUtil
import com.example.myweatherapp.utils.Constant
import java.lang.Math.ceil
import java.text.SimpleDateFormat
import java.util.*


class DaysAdapter (
    var dtToday:Int

): ListAdapter<Daily, DaysAdapter.ViewHolder>(DailyDiffUtil())  {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
        val binding: CardDaysBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.card_days,parent,false)
        return ViewHolder(binding)
    }



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val day=getItem(position)

        if (getCurrentDayInt(dtToday)==getCurrentDayInt(day.dt)){
            holder.binding.layoutDay.setBackgroundResource(R.drawable.gradient_card_bg)
            holder.binding.textDay.setTextColor(ContextCompat.getColor(context, R.color.light_blue))
            holder.binding.textWeather.setTextColor(ContextCompat.getColor(context, R.color.light_blue))
            holder.binding.textTemp.setTextColor(ContextCompat.getColor(context, R.color.light_blue))
        }

        holder.binding.daily=day
        if(Constant.myPref.appUnit=="metric") {
            holder.binding.textTemp.text= "${ceil(day.temp.min).toInt()} - ${ceil(day.temp.max).toInt()} ${context.getResources().getString(R.string.c)}"
        }else if(Constant.myPref.appUnit=="standard"){
            holder.binding.textTemp.text= "${ceil(day.temp.min).toInt()} - ${ceil(day.temp.max).toInt()} ${context.getResources().getString(R.string.k)}"
        }else if(Constant.myPref.appUnit=="imperial"){
            holder.binding.textTemp.text= "${ceil(day.temp.min).toInt()} - ${ceil(day.temp.max).toInt()} ${context.getResources().getString(R.string.f)}"
        }
        holder.binding.textWeather.text= day.weather[0].description
        holder.binding.textDay.text=getCurrentDay(day.dt)
    }

    class ViewHolder(val binding: CardDaysBinding) : RecyclerView.ViewHolder(binding.root)
    fun getCurrentDayInt( dt: Int) : Int{
        var date= Date(dt*1000L)
        var sdf= SimpleDateFormat("d")
        sdf.timeZone= TimeZone.getDefault()
        var formatedData=sdf.format(date)
        return formatedData.toInt()
    }
    fun getCurrentDay( dt: Int) : String{
        var date= Date(dt*1000L)
        var sdf= SimpleDateFormat("d")
        sdf.timeZone= TimeZone.getDefault()
        var formatedData=sdf.format(date)
        var intDay=formatedData.toInt()
        var calendar=Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH,intDay)
        var format=SimpleDateFormat("EEEE")
        return format.format(calendar.time)
    }
}

