package com.example.myweatherapp

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.legacy.content.WakefulBroadcastReceiver
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.datasource.Repository
import com.example.myweatherapp.home.homeviewmodel.HomeViewModel
import com.example.myweatherapp.home.homeviewmodel.HomeViewModelFactory
import com.example.myweatherapp.model.ApiState
import com.example.myweatherapp.notifications.notificationviewmodel.NotificationViewModel
import com.example.myweatherapp.ui.MainActivity
import com.example.myweatherapp.utils.Constant
import com.example.myweatherapp.utils.MyApp
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlarmReceiver : WakefulBroadcastReceiver(){
    private val viewModel: NotificationViewModel by lazy {NotificationViewModel() }
    val myLocation = Constant.myPref.myLocation
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val alertId=intent.getIntExtra("alert",-1)
        var i = Intent(context, MainActivity::class.java)
        intent?.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        var pendingIntent: PendingIntent = PendingIntent.getActivity(context,alertId, i,0)
        //7 // DELETE FROM ROOM

        CoroutineScope(Dispatchers.IO).launch {
              var result=viewModel.getAlertRemote()
            viewModel.stateFlow.collectLatest { result ->
                when (result) {
                    is ApiState.Loading -> {
                        Log.e("ehba2a","loading...")

                    }
                    is ApiState.Failure -> {
                        Log.e("ehba2a","failed")
                    }
                    is ApiState.Succcess -> {
                        var builder = NotificationCompat.Builder(context,"sara")
                            .setSmallIcon(R.drawable.rain)
                            .setContentTitle(result.data.timezone)
                            .setContentText(result.data.current.weather[0].description)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setContentIntent(pendingIntent)
                        val  notificationManeger = NotificationManagerCompat.from(context)
                        notificationManeger.notify(alertId,builder.build())

                    }
                }
            }


        }

    }
}
class RemoveNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notificationId", -1)
        Toast.makeText(context, notificationId.toString(), Toast.LENGTH_SHORT).show()
        val  notificationManeger = NotificationManagerCompat.from(context)
        notificationManeger.cancel(notificationId)
    }
}