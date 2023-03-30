package com.example.myweatherapp.notifications.notificationview

import androidx.recyclerview.widget.DiffUtil
import com.example.myweatherapp.model.MyAlert

class AlertDiffUtil: DiffUtil.ItemCallback<MyAlert>() {
    override fun areItemsTheSame(oldItem: MyAlert, newItem: MyAlert): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: MyAlert, newItem: MyAlert): Boolean {
        return oldItem == newItem
    }
}