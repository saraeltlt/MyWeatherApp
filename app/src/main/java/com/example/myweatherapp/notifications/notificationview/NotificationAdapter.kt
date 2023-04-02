package com.example.myweatherapp.notifications.notificationview

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.CardNotificationsBinding
import com.example.myweatherapp.model.MyAlert
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter (
    var action: OnNotifClickListner

): ListAdapter<MyAlert, NotificationAdapter.ViewHolder>(AlertDiffUtil())  {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
        val binding: CardNotificationsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.card_notifications,parent,false)
        return ViewHolder(binding)
    }



    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val alertItem=getItem(position)
        if (alertItem.type=="Alarm"){
            val drawable = ContextCompat.getDrawable(context, R.drawable.alarm_icon)
            val bitmap = (drawable as BitmapDrawable).bitmap
            holder.binding.imageType.setImageBitmap(bitmap)

        }else{
            val drawable = ContextCompat.getDrawable(context, R.drawable.notifi_icon)
            val bitmap = (drawable as BitmapDrawable).bitmap
            holder.binding.imageType.setImageBitmap(bitmap)

        }
        holder.binding.textTitle.text="${alertItem.event}  ${alertItem.type}"
        holder.binding.textFromTime.text=timeFormate(alertItem.startTime)
        holder.binding.textToTime.text=timeFormate(alertItem.endTime)
        holder.binding.textFromDate.text=dateFormate(alertItem.startDate)
        holder.binding.textToDate.text=dateFormate(alertItem.endDate)
        holder.binding.alert=alertItem
        holder.binding.action=action
    }

    class ViewHolder(val binding: CardNotificationsBinding) : RecyclerView.ViewHolder(binding.root)

}

interface OnNotifClickListner {
    fun onDeleteClick(alert: MyAlert)
}
fun dateFormate(date : Long): String{
    val selectedCalendar = Calendar.getInstance()
    selectedCalendar.timeInMillis = date
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(selectedCalendar.time)
}
fun timeFormate(time : Long): String{
    val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val timeF = format.format(time)
    return timeF
}