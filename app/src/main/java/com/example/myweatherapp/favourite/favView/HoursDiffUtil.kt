package com.example.myweatherapp.favourite.favView

import androidx.recyclerview.widget.DiffUtil
import com.example.myweatherapp.model.Hourly

class HoursDiffUtil: DiffUtil.ItemCallback<Hourly>() {
    override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem == newItem
    }
}