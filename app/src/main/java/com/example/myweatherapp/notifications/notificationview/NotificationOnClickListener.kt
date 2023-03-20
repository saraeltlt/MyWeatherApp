package com.example.myweatherapp.notifications.notificationview

import com.example.myweatherapp.notifications.notificationmodel.Alert


interface NotificationOnClickListener {
    fun onOptionClicked(alert: Alert)
}