package com.example.myweatherapp.viewmodel.notificationviewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myweatherapp.viewmodel.FakeTestRepo
import com.example.myweatherapp.MainDispatcherRule
import com.example.myweatherapp.getOrAwaitValue
import com.example.myweatherapp.model.MyAlert
import com.example.myweatherapp.notifications.notificationviewmodel.NotificationViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import java.util.*

class NotificationViewModelTest {
    @get:Rule
    var instance = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainRule = MainDispatcherRule()

    private lateinit var viewModel: NotificationViewModel
    private lateinit var repo: FakeTestRepo
    private lateinit var alert1: MyAlert
    private lateinit var alert2: MyAlert

    fun generateDummyData(){
        val calendarFrom = Calendar.getInstance()
        calendarFrom.set(2022,1,1,12,0,0)
        val calendarTo = Calendar.getInstance()
        calendarTo.set(2023,4,2,10,0,0)
        alert1 = MyAlert(
            description = "Test Alert 1",
            start = calendarTo.timeInMillis,
            end = calendarFrom.timeInMillis,
            event = "rain",
            type = "notification"
        )

        //....
        calendarFrom.set(2022,1,1,12,0,0)
        calendarTo.set(2023,4,2,10,0,0)
        alert2 = MyAlert(
            description = "Test Alert 2",
            start = calendarTo.timeInMillis,
            end = calendarFrom.timeInMillis,
            event = "snow",
            type = "alert"
        )

    }
    @Before
    fun setup() {
        //generating dummy data
        generateDummyData()
        repo = FakeTestRepo()
        viewModel = NotificationViewModel(repo)
    }

    @Test
    fun getAllAlert_noInput_returnAll() = runBlockingTest {
        // Given
        repo.insertAlert(alert1)
        repo.insertAlert(alert2)

        // When
        viewModel.getAllAlert()
        var result =viewModel.myAlert.getOrAwaitValue {  }

        // Then
      assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
    }

    @Test
    fun deleteAlert_insureDeleted()= runBlockingTest {
        // Given
        repo.insertAlert(alert1)
        repo.insertAlert(alert2)

        // When
        viewModel.deleteAlert(alert1)
        var result =viewModel.myAlert.getOrAwaitValue {  }

        // Then
        Assertions.assertThat(result).isEqualTo(listOf(alert2))
    }

    @Test
    fun addAlert_LiveDataHasValue() {

        // Given
        //already declared variables

        // When
        viewModel.addAlert(alert1)
        var result =viewModel.myAlert.getOrAwaitValue {  }

        // Then
        assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
    }


}