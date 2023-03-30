package com.example.myweatherapp.model

import androidx.room.*

@Entity(tableName = "myAlerts")
data class MyAlert(
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