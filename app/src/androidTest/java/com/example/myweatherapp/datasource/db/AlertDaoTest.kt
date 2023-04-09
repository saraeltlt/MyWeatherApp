package com.example.myweatherapp.datasource.db
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.myweatherapp.model.Forecast
import com.example.myweatherapp.model.MyAlert
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class AlertDaoTest {
    private lateinit var database: WeatherDB
    private lateinit var alertDao: AlertDAO
    private lateinit var alert:MyAlert


    fun generateDummyData(){

        val calendarFrom = Calendar.getInstance()
        calendarFrom.set(2022,1,1,12,0,0)
        val calendarTo = Calendar.getInstance()
        calendarTo.set(2023,4,2,10,0,0)
        alert = MyAlert(
            description = "Test Alert",
            start = calendarTo.timeInMillis,
            end = calendarFrom.timeInMillis,
            event = "Test Event",
            type = "Test Type"
        )
    }
    @Before
    fun setUp() {
        generateDummyData()
        database = Room.inMemoryDatabaseBuilder(getApplicationContext(), WeatherDB::class.java).build()
        alertDao = database.AlertDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndGetAlerts_creatAndInsert_returnAlert() = runBlocking {
        //Given
        //already decleared in before
        var allAlerts:List<MyAlert>?=null

        //When
        alertDao.insertAlert(alert)
        val alertsFlow = alertDao.getAllAlerts()
        alertsFlow.collect(){
            allAlerts=it
        }
         //Then
        // Verify that the alert was inserted correctly
        assertEquals(1, allAlerts?.size)
        assertEquals(alert.description, allAlerts?.get(0)?.description)
        assertEquals(alert.start, allAlerts?.get(0)?.start)
        assertEquals(alert.end, allAlerts?.get(0)?.end)
        assertEquals(alert.event, allAlerts?.get(0)?.event)
        assertEquals(alert.type, allAlerts?.get(0)?.type)
    }

    @Test
    fun deleteAlert_insertAlert_ReturnZero() = runBlocking {
        //Given
        //already decleared in before
        var allAlerts:List<MyAlert>?=null

        //When
        alertDao.insertAlert(alert)
        alertDao.deleteAlert(alert)
        alertDao.insertAlert(alert)
        val alertsFlow = alertDao.getAllAlerts()
        alertsFlow.collect(){
            allAlerts=it
        }

      //Then
        assertEquals(0, allAlerts?.size)
    }

}







