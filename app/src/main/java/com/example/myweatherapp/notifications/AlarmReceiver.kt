@file:Suppress("DEPRECATION")

package com.example.myweatherapp.notifications

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.legacy.content.WakefulBroadcastReceiver
import com.example.myweatherapp.R
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

var windowManager:WindowManager?=null
var mediaPlayer:MediaPlayer?=null
val view: View?=null
class AlarmReceiver : WakefulBroadcastReceiver() {
    private val viewModel: NotificationViewModel by lazy { NotificationViewModel() }
    val myLocation = Constant.myPref.myLocation

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission", "InvalidWakeLockTag")
    override  fun onReceive(context: Context, intent: Intent) {
        val alertId = intent.getIntExtra("alert", -1)
        var alertEvent = intent.getStringExtra("alert2")
           alertEvent=getEvent(context,alertEvent)
        val alertType = intent.getStringExtra("alert3")



        var i = Intent(context, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        var pendingIntent: PendingIntent = PendingIntent.getActivity(context, alertId, i, 0)
        if ( Constant.myPref.alarmFlag==true) {
            if (NetworkManager.isInternetConnected()) {

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
                                    .setLargeIcon(
                                        BitmapFactory.decodeResource(
                                            context.getResources(),
                                            R.drawable.server_error
                                        )
                                    )
                                    .setContentTitle(
                                        context.getResources().getString(R.string.connProblem)
                                    )
                                    .setContentText(
                                        context.getResources()
                                            .getString(R.string.connProblemDesc)
                                    )
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setAutoCancel(true)
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setContentIntent(pendingIntent)
                                val notificationManeger =
                                    NotificationManagerCompat.from(context)
                                notificationManeger.notify(alertId, builder.build())
                            }
                            is ApiState.Succcess -> {
                                if (alertType == "n") {
                                    //empty list
                                    if (result.data.alerts.isEmpty()) {
                                        var builder =
                                            NotificationCompat.Builder(context, "sara")
                                                .setSmallIcon(R.drawable.rain)
                                                .setLargeIcon(
                                                    BitmapFactory.decodeResource(
                                                        context.getResources(),
                                                        R.drawable.rain
                                                    )
                                                )
                                                .setContentTitle(alertEvent)
                                                .setContentText(
                                                    context.getResources()
                                                        .getString(R.string.no_aler)
                                                )
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
                                        var builder =
                                            NotificationCompat.Builder(context, "sara")
                                                .setSmallIcon(R.drawable.rain)
                                                .setLargeIcon(
                                                    BitmapFactory.decodeResource(
                                                        context.getResources(),
                                                        R.drawable.rain
                                                    )
                                                )
                                                .setContentTitle(alertEvent)
                                                .setContentText(
                                                    alertEvent + context.getResources()
                                                        .getString(R.string.alert_detect)
                                                )
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
                                        var builder =
                                            NotificationCompat.Builder(context, "sara")
                                                .setSmallIcon(R.drawable.rain)
                                                //.setLargeIcon()
                                                .setContentTitle(alertType)
                                                .setContentText(
                                                    context.getResources()
                                                        .getString(R.string.no) + alertType + context.getResources()
                                                        .getString(R.string.alert_but) + result.data.current.weather[0].description + context.getResources()
                                                        .getString(R.string.alert_detect)
                                                )
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                .setAutoCancel(true)
                                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .setContentIntent(pendingIntent)
                                        val notificationManeger =
                                            NotificationManagerCompat.from(context)
                                        notificationManeger.notify(alertId, builder.build())
                                    }
                                } else if (alertType == "a") {
                                    alertFire(context, result, alertEvent, alertType)
                                }

                            }
                        }
                    }


                }


            } else {
                var builder = NotificationCompat.Builder(context, "sara")
                    .setSmallIcon(R.drawable.rain)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            context.getResources(),
                            R.drawable.wifi
                        )
                    )
                    .setContentTitle(context.getResources().getString(R.string.NoInter))
                    .setContentText(context.getResources().getString(R.string.NoInterDetail))
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

    private suspend fun alertFire(
        context: Context,
        result: ApiState.Succcess,
        alertEvent: String?,
        alertType: String
    ) {

        val view: View = LayoutInflater.from(context).inflate(R.layout.alert_dialog_layout, null, false)
        val dismissBtn = view.findViewById(R.id.dissmiss) as Button
        val textTitle = view.findViewById(R.id.titleText) as TextView
        val textdetails = view.findViewById(R.id.detailsText) as TextView

        if (NetworkManager.isInternetConnected()) {
            if (result.data.alerts.isEmpty()) {
                textTitle.text= alertEvent
                textdetails.text=context.getResources().getString(R.string.no_aler)

            }
            else if (result.data.alerts[0].event == alertEvent) {
                textTitle.text= alertEvent
                textdetails.text=  alertEvent + context.getResources().getString(R.string.alert_detect)
            }
            else {
                textTitle.text= alertEvent
                textdetails.text= context.getResources()
                    .getString(R.string.no) + alertType + context.getResources()
                    .getString(R.string.alert_but) + result.data.current.weather[0].description + context.getResources()
                    .getString(R.string.alert_detect)
            }
        }
        //no internet
        else{
            textTitle.text=context.getResources().getString(R.string.NoInter)
            textdetails.text=context.getResources().getString(R.string.NoInterDetail)
        }



            // set the window parameters
            val windowParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                },
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT
            )
            windowParams.gravity = Gravity.TOP

        withContext(Dispatchers.Main) {
            windowManager?.addView(view, windowParams)
            view.visibility = View.VISIBLE
        }


            mediaPlayer?.start()
            mediaPlayer?.isLooping = true


        dismissBtn.setOnClickListener {
            mediaPlayer?.release()
            windowManager?.removeView(view)
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
class RemoveAlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        mediaPlayer?.release()
        windowManager?.removeView(view)
    }
}
fun getEvent(context: Context, selectedItem: String?):String{
    if (selectedItem== context?.getResources()?.getString(R.string.rain)){
        return "rain"
    }
    else if (selectedItem== context?.getResources()?.getString(R.string.storm)){
        return "storm"
    }
    else if (selectedItem== context?.getResources()?.getString(R.string.heat_advisory)){
        return "heat advisory"
    }
    else if (selectedItem== context?.getResources()?.getString(R.string.heat)){
        return "heat"
    }
    else if (selectedItem== context?.getResources()?.getString(R.string.snow)){
        return "snow"
    }
    else if (selectedItem== context?.getResources()?.getString(R.string.fog)){
        return "fog"
    }
    else if (selectedItem== context?.getResources()?.getString(R.string.thunderstorm)){
        return "thunderstorm"
    }
    else if (selectedItem== context?.getResources()?.getString(R.string.cloud)){
        return "cloud"
    }
    else if (selectedItem== context?.getResources()?.getString(R.string.wind)){
        return "wind"
    }
    else if (selectedItem== context?.getResources()?.getString(R.string.showerRain)){
        return "shower rain"
    }
    else if (selectedItem== context?.getResources()?.getString(R.string.fewClouds)){
        return "few clouds"
    }
    else if (selectedItem== context?.getResources()?.getString(R.string.scatteredClouds)){
        return "scattered Clouds"
    }
    else if (selectedItem== context?.getResources()?.getString(R.string.brokenClouds)){
        return "broken Clouds"
    }
    else if (selectedItem== context?.getResources()?.getString(R.string.mist)){
        return "mist"
    }
    return selectedItem!!
}

