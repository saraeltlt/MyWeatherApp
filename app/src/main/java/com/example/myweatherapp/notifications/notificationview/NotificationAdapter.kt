package com.example.myweatherapp.notifications.notificationview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.CardNotificationsBinding
import com.example.myweatherapp.notifications.notificationmodel.Alert

class NotificationAdapter (
    var action: OnNotifClickListner

): ListAdapter<Alert, NotificationAdapter.ViewHolder>(AlertDiffUtil())  {
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
        holder.binding.textTitle.text="${alertItem.event} + ${alertItem.type}"
        holder.binding.textFromTime.text=alertItem.startTime
        holder.binding.textToTime.text=alertItem.endTime
        holder.binding.textFromDate.text=alertItem.startDate
        holder.binding.textToDate.text=alertItem.endDate
        holder.binding.alert=alertItem
        holder.binding.action=action
    }

    class ViewHolder(val binding: CardNotificationsBinding) : RecyclerView.ViewHolder(binding.root)

}

interface OnNotifClickListner {
    fun onDeleteClick(alert: Alert)
}