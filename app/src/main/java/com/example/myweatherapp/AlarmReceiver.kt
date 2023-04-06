@file:Suppress("DEPRECATION")

package com.example.myweatherapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.util.Log
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
                              if (result.data.alerts[0].event == null) {
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
                // create and show the alert dialog
                val alertDialog = AlertDialog.Builder(context)
                    .setTitle("My Alert Dialog")
                    .setMessage("This is my custom alert dialog")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()

                // create a MediaPlayer object to play the alarm sound
                val mediaPlayer = MediaPlayer.create(context, R.raw.alarm)
                //mediaPlayer. = true

                // show the dialog as a system alert window
               //  or TYPE_APPLICATION_PANEL
                alertDialog.window?.setType(WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY)
                alertDialog.show()

                // start playing the alarm sound
                mediaPlayer.start()

                // ensure that the device stays awake during the alarm
                /*val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakefulAlertDialogReceiver")
                wakeLock.acquire(10 * 60 * 1000L /* 10 minutes */)*/

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