package com.example.myweatherapp.datasource.db

import androidx.room.*
import com.example.myweatherapp.model.MyAlert
import kotlinx.coroutines.flow.Flow
@Dao
interface AlertDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlert(myAlert: MyAlert)
    @Delete
    fun deleteAlert(myAlert: MyAlert)
    @Query("select* from myAlerts")
    fun getAllAlerts(): Flow<List<MyAlert>>
}