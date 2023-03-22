package com.example.myweatherapp.notifications.notificationmodel

import androidx.room.*

@Entity(tableName = "alerts")
data class Alert(
    val description: String,
    val startDate: String,
    val endDate: String,
    val startTime: String,
    val endTime: String,
    val event: String,
    val type: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}