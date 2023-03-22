package com.example.myweatherapp.notifications.notificationview

import androidx.recyclerview.widget.DiffUtil
import com.example.myweatherapp.notifications.notificationmodel.Alert

class AlertDiffUtil: DiffUtil.ItemCallback<Alert>() {
    override fun areItemsTheSame(oldItem: Alert, newItem: Alert): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Alert, newItem: Alert): Boolean {
        return oldItem == newItem
    }
}