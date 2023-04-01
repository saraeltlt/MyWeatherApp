package com.example.myweatherapp.model

import androidx.room.*

@Entity(tableName = "myAlerts")
data class MyAlert(
    var description: String,
    var startDate: Long,
    var endDate: Long,
    var startTime: Long,
    var endTime: Long,
    var event: String,
    var type: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}