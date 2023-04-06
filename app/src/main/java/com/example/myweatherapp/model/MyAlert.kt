package com.example.myweatherapp.model

import androidx.room.*


@Entity(tableName = "myAlerts")
data class MyAlert(
    var description: String,
    var start: Long,
    var end: Long,
    var event: String,
    var type: String,
    @PrimaryKey
    var myId: Int=0
): java.io.Serializable