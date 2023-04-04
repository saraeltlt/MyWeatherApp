package com.example.myweatherapp

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myweatherapp.ui.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val alertId=intent.getIntExtra("alert",-1)

        Log.e("yarab",alertId.toString())

        var i = Intent(context, MainActivity::class.java)
        intent?.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        var pendingIntent: PendingIntent = PendingIntent.getActivity(context,alertId, i,0)
        var builder = NotificationCompat.Builder(context,"fox")
            .setSmallIcon(R.drawable.rain)
            .setContentTitle("sarsor")
            .setContentText("ehhh" + alertId)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
       val  notificationManeger = NotificationManagerCompat.from(context)
        notificationManeger.notify(alertId.toInt(),builder.build())
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