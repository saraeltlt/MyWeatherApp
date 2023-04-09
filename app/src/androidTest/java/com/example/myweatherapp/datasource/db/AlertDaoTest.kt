package com.example.myweatherapp.datasource.db
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.myweatherapp.model.MyAlert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.Is
import org.junit.*
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class AlertDaoTest {
    private lateinit var database: WeatherDB
    private lateinit var alertDao: AlertDAO
    private lateinit var alert:MyAlert
    private lateinit var alert2:MyAlert

    @get:Rule
    var instance = InstantTaskExecutorRule()

    fun generateDummyData(){
        val calendarFrom = Calendar.getInstance()
        calendarFrom.set(2022,1,1,12,0,0)
        val calendarTo = Calendar.getInstance()
        calendarTo.set(2023,4,2,10,0,0)
        alert = MyAlert(
            description = "Test Alert 1",
            start = calendarTo.timeInMillis,
            end = calendarFrom.timeInMillis,
            event = "rain",
            type = "notification"
        )


    }
    @Before
    fun setUp() {
        generateDummyData()
        database = Room.inMemoryDatabaseBuilder(getApplicationContext(), WeatherDB::class.java)
            .allowMainThreadQueries().build()
        alertDao = database.AlertDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getAlerts_InsertAlerts_countItems() = runBlockingTest {
        //Given
        //already decleared in before
        alertDao.insertAlert(alert)

        //When
        val result = alertDao.getAllAlerts().first()

        //Then
        assertThat(result.size, Is.`is`(1))
    }
    @Test
    fun insertAlerts_InsertSingleAlertItem_returnItems() = runBlockingTest {
        //Given
        //already decleared in before

        //When
        alertDao.insertAlert(alert)


        //Then
        val result = alertDao.getAllAlerts().first()
        assertThat(result.get(0), not(nullValue()))
    }
    @Test
    fun deleteAlert_deleteAlert_checkIsNull() = runBlockingTest{
        //Given
        //already decleared in before
        alertDao.insertAlert(alert)

        //When
        alertDao.deleteAlert(alert)

      //Then
        val result = alertDao.getAllAlerts().first()
        assertThat(result, IsEmptyCollection.empty())
    }

}







