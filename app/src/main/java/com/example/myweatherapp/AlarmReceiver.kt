@file:Suppress("DEPRECATION")

package com.example.myweatherapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
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
import kotlinx.coroutines.withContext

class AlarmReceiver : WakefulBroadcastReceiver(){
    private val viewModel: NotificationViewModel by lazy {NotificationViewModel() }
    val myLocation = Constant.myPref.myLocation
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission", "InvalidWakeLockTag")
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
            if (alertType=="n") {
                CoroutineScope(Dispatchers.IO).launch {
                    var result = viewModel.getAlertRemote()
                    viewModel.stateFlow.collectLatest { result ->
                        when (result) {
                            is ApiState.Loading -> {
                                Log.e("SE", "loading...")
                            }
                            is ApiState.Failure -> {
                                var builder = NotificationCompat.Builder(context, "sara")
                                    .setSmallIcon(R.drawable.rain)
                                    .setLargeIcon(BitmapFactory. decodeResource (context.getResources() , R.drawable.server_error))
                                    .setContentTitle(context.getResources().getString(R.string.connProblem))
                                    .setContentText(context.getResources().getString(R.string.connProblemDesc))
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setAutoCancel(true)
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setContentIntent(pendingIntent)
                                val notificationManeger = NotificationManagerCompat.from(context)
                                notificationManeger.notify(alertId, builder.build())
                            }
                            is ApiState.Succcess -> {
                                //empty list
                              if (result.data.alerts.isEmpty()) {
                                    var builder = NotificationCompat.Builder(context, "sara")
                                        .setSmallIcon(R.drawable.rain)
                                        .setLargeIcon(BitmapFactory. decodeResource (context.getResources() , R.drawable.rain))
                                        .setContentTitle(alertEvent)
                                        .setContentText(context.getResources().getString(R.string.no_aler))
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setAutoCancel(true)
                                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setContentIntent(pendingIntent)
                                    val notificationManeger =
                                        NotificationManagerCompat.from(context)
                                    notificationManeger.notify(alertId, builder.build())
                                }
                                //same alert
                               else if (result.data.alerts[0].event == alertEvent) {
                                    var builder = NotificationCompat.Builder(context, "sara")
                                        .setSmallIcon(R.drawable.rain)
                                        .setLargeIcon(
                                            BitmapFactory.decodeResource(
                                                context.getResources(),
                                                R.drawable.rain
                                            )
                                        )
                                        .setContentTitle(alertEvent)
                                        .setContentText(alertEvent+  context.getResources().getString(R.string.alert_detect))
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setAutoCancel(true)
                                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setContentIntent(pendingIntent)
                                    val notificationManeger =
                                        NotificationManagerCompat.from(context)
                                    notificationManeger.notify(alertId, builder.build())
                                }

                                //different alert
                                else {
                                    var builder = NotificationCompat.Builder(context, "sara")
                                        .setSmallIcon(R.drawable.rain)
                                        //.setLargeIcon()
                                        .setContentTitle(alertType)
                                        .setContentText(context.getResources().getString(R.string.no) +alertType+context.getResources().getString(R.string.alert_but) +result.data.current.weather[0].description+ context.getResources().getString(R.string.alert_detect))
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setAutoCancel(true)
                                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setContentIntent(pendingIntent)
                                    val notificationManeger =
                                        NotificationManagerCompat.from(context)
                                    notificationManeger.notify(alertId, builder.build())
                                }

                            }
                        }
                    }


                }
            }
            else if (alertType=="a"){
                CoroutineScope(Dispatchers.Main ).launch {
                    alertFire(context)
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

    private suspend fun alertFire(context: Context) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.alarm)

        val view: View = LayoutInflater.from(context).inflate(R.layout.empty, null, false)
        /* val dismissBtn = view.findViewById(R.id.btnDismissAlarm) as Button
         val textView = view.findViewById(R.id.descriptionAlarm) as TextView*/
        val layoutParams =
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT

            )
        layoutParams.gravity = Gravity.TOP

        val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager

        withContext(Dispatchers.Main) {
            windowManager.addView(view, layoutParams)
            view.visibility = View.VISIBLE
            // textView.text = message
        }

        mediaPlayer.start()
        mediaPlayer.isLooping = true
        /* dismissBtn.setOnClickListener {
             mediaPlayer?.release()
             windowManager.removeView(view)
         }
         repository.deleteAlert(entityAlert)*/
    }
}
class RemoveNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notificationId", -1)
        val  notificationManeger = NotificationManagerCompat.from(context)
        notificationManeger.cancel(notificationId)
    }
}