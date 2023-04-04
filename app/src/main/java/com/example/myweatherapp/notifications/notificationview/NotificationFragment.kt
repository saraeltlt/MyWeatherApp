package com.example.myweatherapp.notifications.notificationview

import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.AlarmReceiver
import com.example.myweatherapp.R
import com.example.myweatherapp.RemoveNotificationReceiver
import com.example.myweatherapp.databinding.FragmentNotificationBinding
import com.example.myweatherapp.model.MyAlert
import com.example.myweatherapp.notifications.notificationviewmodel.NotificationViewModel
import com.example.myweatherapp.notifications.notificationviewmodel.NotificationViewModelFactory
import com.example.myweatherapp.ui.ConfirmDeleteInterface
import com.example.myweatherapp.ui.ConfirmDialogFragment
import com.example.myweatherapp.utils.Constant
import com.example.myweatherapp.utils.MyApp
import com.example.myweatherapp.utils.Permissions
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


class NotificationFragment : Fragment() , OnNotifClickListner, Dialoge.SaveAlertInterface,
    ConfirmDeleteInterface {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var dialog: Dialoge
    lateinit var viewModel: NotificationViewModel
    lateinit var factory: NotificationViewModelFactory
    lateinit var adapter: NotificationAdapter
    var alarmManager: AlarmManager?=null
    var pendingIntent: PendingIntent ?=null
    companion object{
        var alertRemove: MyAlert?=null
        var alertSet: MyAlert?=null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNav=requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.visibility= View.VISIBLE
        val viewLine=requireActivity().findViewById<View>(R.id.viewLine)
        viewLine.visibility= View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_notification,container,false) as FragmentNotificationBinding
        binding.lifecycleOwner=this
        val view = binding.root
        factory = NotificationViewModelFactory(
            MyApp.getInstanceRepository()
        )
        createNotificationsChannel()
        adapter= NotificationAdapter(this)
        binding.notifRecycler.adapter=adapter
        binding.notifRecycler.layoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        viewModel = ViewModelProvider(this,factory).get(NotificationViewModel::class.java)
        viewModel.myAlert.observe(viewLifecycleOwner, Observer{
            if (it.isEmpty()){
                binding.notifRecycler.visibility= View.GONE
                binding.animationView.visibility= View.VISIBLE
                binding.txtAlert.visibility=View.VISIBLE

            }
            else {
                binding.animationView.visibility= View.GONE
                binding.txtAlert.visibility=View.GONE
                binding.notifRecycler.visibility= View.VISIBLE
                adapter.submitList(it)
            }
        })

        binding.notifFab.setOnClickListener {
            showAlertDialog()
        }



        return view
    }

    private fun createNotificationsChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("fox", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val  notificationManager=
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun   showAlertDialog(){
     dialog= Dialoge(requireContext(),this)
     dialog.show(parentFragmentManager, "alertDialog")

 }

    override fun onDeleteClick(myAlert: MyAlert) {
        alertRemove=myAlert
        var dialog = ConfirmDialogFragment(this)
        dialog.show(childFragmentManager, "alertDialog")
    }



    override fun onClick(confirmDelete: Boolean) {
        if (confirmDelete) {
           viewModel.deleteAlert(alertRemove!!)
            Toast.makeText(requireContext(),R.string.removedFromAlerts, Toast.LENGTH_LONG).show()
            cancelAlarm(alertRemove!!)
        }
    }

    private fun cancelAlarm(alertRemove: MyAlert) {
        val intent =Intent(requireContext(),AlarmReceiver::class.java)
        pendingIntent=PendingIntent.getBroadcast(requireContext(),alertRemove.myId,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        if (alarmManager==null){
            alarmManager=   requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
        alarmManager!!.cancel(pendingIntent)

    }

    override fun onClickSave(myAlert: MyAlert) {
        myAlert.myId=generateUniqueIntValue(myAlert.end,myAlert.start,myAlert.description,myAlert.event)
        Log.e("haaa",myAlert.myId.toString())
        viewModel.addAlert(myAlert)
        if (Permissions.checkPremissionNotifications(requireContext())){
            setAlarm(myAlert)
        }else{
            requestPermission()
        }




    }

    private fun setAlarm(myAlert: MyAlert) {
        alertSet=myAlert
        alarmManager=   requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent =Intent(requireContext(),AlarmReceiver::class.java)
        intent.putExtra("alert",myAlert.myId)
        Log.e("yarab",myAlert.toString())
        pendingIntent=PendingIntent.getBroadcast(requireContext(),myAlert.myId,intent,0)
       // alarmManager.set
        alarmManager!!.setExact(AlarmManager.RTC_WAKEUP,myAlert.start,pendingIntent)
      //  alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, myAlert.startTime, AlarmManager.INTERVAL_DAY,pendingIntent)
        Toast.makeText(requireContext(), "alert set succes",Toast.LENGTH_SHORT).show()

        removeNotifcationAfter(myAlert)

    }

    private fun removeNotifcationAfter(myAlert: MyAlert) {
        val removeNotificationIntent = Intent(requireContext(), RemoveNotificationReceiver::class.java)
        removeNotificationIntent.putExtra("notificationId",myAlert.myId)

        val removeNotificationPendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            myAlert.myId,
            removeNotificationIntent,
            0
        )

        val delayInMillis = 1 * 60 * 1000L // 5 minutes
        alarmManager!!.setExact(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + delayInMillis,
            removeNotificationPendingIntent
        )
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(
            "android.permission.POST_NOTIFICATIONS",
            "android.permission.SYSTEM_ALERT_WINDOW"
        ), Constant.LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setAlarm(alertSet!!)
        }
    }
    fun generateUniqueIntValue(a: Long, b: Long, str: String, strType:String): Int {
        val input = "$a$b$str$strType"
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray(StandardCharsets.UTF_8))
        val truncatedHash = hash.copyOfRange(0, 4) // Truncate hash to 4 bytes
        return truncatedHash.fold(0) { acc, byte -> (acc shl 8) + (byte.toInt() and 0xff) }
    }
}