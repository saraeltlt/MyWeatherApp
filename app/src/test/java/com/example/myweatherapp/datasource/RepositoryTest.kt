package com.example.myweatherapp.datasource

import com.example.myweatherapp.MainDispatcherRule
import com.example.myweatherapp.getOrAwaitValue
import com.example.myweatherapp.model.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)

 class RepositoryTest {
   private lateinit var forecast1:Forecast
   private lateinit var forecast2:Forecast
   private lateinit var repository: Repository
   private lateinit var remoteSource: FakeRemoteSource
   private lateinit var localSource: FakeLocalSource
   private lateinit var testDispatcher: TestCoroutineDispatcher

   @ExperimentalCoroutinesApi
   @get:Rule
   var mainRule = MainDispatcherRule()
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
      testDispatcher = TestCoroutineDispatcher()
      generateDummyData()
      remoteSource = FakeRemoteSource()
      localSource = FakeLocalSource()
      repository = Repository(remoteSource, localSource)
   }

   @Test
   fun getCurrentWeather_true_RemoteSource() = runBlockingTest {
      // Given
      val expectedForecast = forecast1
      //When
     // remoteSource.favWeather.add(forecast1)
      val stateFlowWeather = repository.getCurrentWeather(37.7749, -122.4194, "en", "metric")
      var actualForecast = stateFlowWeather.getOrAwaitValue {  }
      //Then
      assertEquals(expectedForecast, actualForecast)
   }



   @Test
   fun getAllFav_returnTrueIfNotEmpty() = runBlockingTest {
      // Given
      localSource.insertFavOrCurrent(forecast1)
      //When
      var result:List<Forecast>?=null
      val favoritesFlow = repository.getAllFav()
      favoritesFlow.collect(){
         result=it
      }
      //Then
      assertThat(result, not(nullValue()))
   }

   @Test
   fun insertFavForecast_addForecast_returnTrue()= runBlockingTest {
      //given
     // already decleared forecast1 in before
      //when
      repository.insertFav(forecast1)
      val favoritesFlow = localSource.getAllFav()
      var  favList:List<Forecast>?=null
      favoritesFlow.collect(){
         favList=it
      }
      //then
      assertEquals(1, favList?.size)
      assertEquals(forecast1, favList?.get(0))
   }


   @Test
   fun deleteFav_takeTheForecast_removeFromLocalSource() =runBlockingTest {
      //given
      // already decleared forecast1 in before

      //when
      repository.insertFav(forecast1)
      repository.deleteFav(forecast1)
      val favoritesFlow = localSource.getAllFav()
      var  favList:List<Forecast>?=null
      favoritesFlow.collect(){
         favList=it
      }
      //then
      assertEquals(0, favList?.size)
   }
}