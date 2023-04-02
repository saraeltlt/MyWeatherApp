package com.example.myweatherapp.model

import androidx.room.*

@Entity(tableName = "myAlerts")
data class MyAlert(
    var description: String,
    var start: Long,
    var end: Long,
    var event: String,
    var type: String,
    var myId: Int=0
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}