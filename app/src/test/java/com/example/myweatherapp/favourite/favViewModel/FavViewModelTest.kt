package com.example.myweatherapp.favourite.favViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myweatherapp.MainDispatcherRule
import com.example.myweatherapp.getOrAwaitValue
import com.example.myweatherapp.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue


class FavViewModelTest {

    @get:Rule
    var instance = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainRule = MainDispatcherRule()

    private lateinit var viewModel: FavViewModel
    private lateinit var repo: FakeTestRepo
    private lateinit var forecast1:Forecast
    private lateinit var forecast2:Forecast


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

         forecast1 = Forecast(
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

        val temp2 = Temp(
            12.3,
            14.5,
            18.0,
            9.5,
            11.0,
            15.5
        )

        val hourly2 = Hourly(
            50,
            1648831200,
            65,
            1012,
            15.5,
            2.5,
            10_000,
            listOf(weather1),
            3.8
        )

        val daily2 = Daily(
            70,
            1648744800,
            60,
            1005,
            0.0,
            0.0,
            temp2,
            5.8,
            listOf(weather1),
            2.1
        )

        val current2 = Current(
            10,
            1648908000,
            40,
            1008,
            20.2,
            8.3,
            8000,
            listOf(weather1),
            3.2
        )

         forecast2 = Forecast(
            emptyList(),
            current2,
            listOf(daily2),
            listOf(hourly2),
            40.7128,
            -74.0060,
            "America/New_York",
            -14400
        )
    }
    @Before
    fun setup() {
        //generating dummy data
         generateDummyData()
        repo = FakeTestRepo()
        viewModel = FavViewModel(repo)
    }


    @Test
    fun getAllFav_noInput_returnAll() = runBlockingTest {
        // Given
        var expectedFavList = listOf(forecast1,forecast1)
        repo.insertFav(forecast1)
        repo.insertFav(forecast2)

        // When
        viewModel.getAllFav()
        var result =viewModel.favList.getOrAwaitValue {  }

        // Then
        assertThat(result, not(nullValue()))
    }


    @Test
    fun deleteFav_insureDeleted()= runBlockingTest {
        // Given
        repo.insertFav(forecast1)
        repo.insertFav(forecast2)

        // When
        viewModel.deleteFav(forecast1)
        var result =viewModel.favList.getOrAwaitValue {  }

        // Then
        assertThat(result).isEqualTo(listOf(forecast2))
    }

    @Test
    fun addFav_LiveDataHasValue() {

        // Given
        //already declared variables

        // When
        viewModel.addFav(forecast1)
        var result =viewModel.favList.getOrAwaitValue {  }

        // Then
        assertThat(result, not(nullValue()))
    }

}