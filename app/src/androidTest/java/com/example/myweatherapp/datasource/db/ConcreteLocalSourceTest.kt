package com.example.myweatherapp.datasource.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.myweatherapp.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class ConcreteLocalSourceTest {
    @get:Rule
    var instance = InstantTaskExecutorRule()

    private lateinit var localSource: ConcreteLocalSource
    private lateinit var forecast: Forecast

    fun generateDummyData() {
        val alert1 = Alert(
            "A severe thunderstorm warning is in effect",
            "Severe Thunderstorm Warning"
        )

        val hourly1 = Hourly(
            75,
            1648686000,
            80,
            1005,
            26.5,
            2.3,
            28,
            listOf(Weather("Overcast", "04d", 804, "Clouds")),
            4.6
        )

        val temp1 = Temp(
            28.5,
            26.7,
            30.2,
            25.5,
            26.1,
            29.9
        )

        val daily1 = Daily(
            65,
            1648838400,
            75,
            1010,
            0.0,
            0.0,
            temp1,
            7.1,
            listOf(Weather("Clear", "01d", 800, "Clear Sky")),
            3.5
        )

        val current1 = Current(
            30,
            1648671600,
            50,
            1012,
            22.6,
            6.7,
            10000,
            listOf(Weather("Clear", "01d", 800, "Clear Sky")),
            2.8
        )

        forecast = Forecast(
            listOf(alert1),
            current1,
            listOf(daily1),
            listOf(hourly1),
            37.7749,
            -122.4194,
            "America/Los_Angeles",
            -25200
        )

        val weather1 = Weather(
            "Overcast",
            "04d",
            804,
            "Clouds"
        )


    }

    @Before
    fun createLocalSource() {
        generateDummyData()
        val context = ApplicationProvider.getApplicationContext<Context>()
        localSource = ConcreteLocalSource.getInstance(context)
    }

    @Test
    fun getFav_InsertFav_checkItem() = runBlockingTest {
        //Given
        //already implmeneted in before
        localSource.insertFavOrCurrent(forecast)

        //when
        val result= localSource.getAllFav().first()

        //Then
        assertEquals(result[0].lat,forecast.lat,2.0)
        assertEquals(result[0].lon,forecast.lon,2.0)
        assertEquals(result[0].timezone,forecast.timezone)

    }

    @Test
    fun insertFav_InsertSingleItem_returnItems() = runBlockingTest {
        //Given
        //already implmeneted in before

        //when
        localSource.insertFavOrCurrent(forecast)


        //Then
        val result= localSource.getAllFav().first()
        MatcherAssert.assertThat(result.get(0), CoreMatchers.not(CoreMatchers.nullValue()))

    }

    @Test
    fun deleteFav_insertFav_ReturnZero() = runBlocking {
        //Given
        //already implmeneted in before
        var allFav: List<Forecast>? = null


        //When
        localSource.insertFavOrCurrent(forecast)
        localSource.deleteFav(forecast)
        val forecastFlow = localSource.getAllFav()
        forecastFlow.collect() {
            allFav = it
        }
        //Then
        assertEquals(0, allFav?.size)
    }
}
