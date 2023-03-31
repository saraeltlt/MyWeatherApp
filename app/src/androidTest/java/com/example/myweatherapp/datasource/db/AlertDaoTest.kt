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

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class AlertDaoTest {
    private lateinit var database: WeatherDB
    private lateinit var alertDao: AlertDAO
    private lateinit var alert:MyAlert


    fun generateDummyData(){
       alert = MyAlert(
            description = "Test Alert",
            startDate = "2022-01-01",
            endDate = "2022-01-01",
            startTime = "10:00",
            endTime = "12:00",
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
        assertEquals(alert.startDate, allAlerts?.get(0)?.startDate)
        assertEquals(alert.endDate, allAlerts?.get(0)?.endDate)
        assertEquals(alert.startTime, allAlerts?.get(0)?.startTime)
        assertEquals(alert.endTime, allAlerts?.get(0)?.endTime)
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







