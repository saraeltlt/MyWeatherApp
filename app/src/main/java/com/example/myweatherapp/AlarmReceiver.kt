package com.example.myweatherapp

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.legacy.content.WakefulBroadcastReceiver
import com.example.myweatherapp.model.ApiState
import com.example.myweatherapp.model.MyAlert
import com.example.myweatherapp.notifications.notificationviewmodel.NotificationViewModel
import com.example.myweatherapp.ui.MainActivity
import com.example.myweatherapp.utils.Constant
import com.example.myweatherapp.utils.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlarmReceiver : WakefulBroadcastReceiver(){
    private val viewModel: NotificationViewModel by lazy {NotificationViewModel() }
    val myLocation = Constant.myPref.myLocation
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val alertId=intent.getIntExtra("alert",-1)
        Log.e("SE",alertId.toString())
       val alertEvent=intent.getStringExtra("alert2")
        Log.e("SE", alertEvent!!)
        val alertType=intent.getStringExtra("alert3")
        Log.e("SE", alertType!!)

        /*val alert=intent.getSerializableExtra("alert1") as MyAlert
        Log.e("SE",alert.event)*/

        var i = Intent(context, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        var pendingIntent: PendingIntent = PendingIntent.getActivity(context,alertId, i,0)
        if (NetworkManager.isInternetConnected()) {
            CoroutineScope(Dispatchers.IO).launch {
                var result = viewModel.getAlertRemote()
                viewModel.stateFlow.collectLatest { result ->
                    when (result) {
                        is ApiState.Loading -> {
                            Log.e("ehba2a", "loading...")

                        }
                        is ApiState.Failure -> {
                            Log.e("ehba2a", "failed")
                        }
                        is ApiState.Succcess -> {
                            if ( result.data.alerts[0].event==alertEvent ) {
                                var builder = NotificationCompat.Builder(context, "sara")
                                    .setSmallIcon(R.drawable.rain)
                                    .setLargeIcon(BitmapFactory. decodeResource (context.getResources() , R.drawable.rain))
                                    .setContentTitle(result.data.timezone)
                                    .setContentText(result.data.current.weather[0].description)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setAutoCancel(true)
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setContentIntent(pendingIntent)
                                val notificationManeger = NotificationManagerCompat.from(context)
                                notificationManeger.notify(alertId, builder.build())
                            }else{
                                var builder = NotificationCompat.Builder(context, "sara")
                                    .setSmallIcon(R.drawable.rain)
                                    //.setLargeIcon()
                                    .setContentTitle(result.data.timezone)
                                    .setContentText(result.data.current.weather[0].description)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setAutoCancel(true)
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setContentIntent(pendingIntent)
                                val notificationManeger = NotificationManagerCompat.from(context)
                                notificationManeger.notify(alertId, builder.build())
                            }

                        }
                    }
                }


            }
        }
        else{
            var builder = NotificationCompat.Builder(context, "sara")
                .setSmallIcon(R.drawable.rain)
                .setLargeIcon(BitmapFactory. decodeResource (context.getResources() , R.drawable.wifi))
                .setContentTitle("No Internet connection")
                .setContentText("can't get the weather status, check your internet connection")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
            val notificationManeger = NotificationManagerCompat.from(context)
            notificationManeger.notify(alertId, builder.build())
        }

    }
}
class RemoveNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notificationId", -1)
        val  notificationManeger = NotificationManagerCompat.from(context)
        notificationManeger.cancel(notificationId)
    }
}