package com.example.myweatherapp.notifications.notificationview

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.AddAlertDialogBinding
import com.example.myweatherapp.databinding.ConfirmDialogBinding
import com.example.myweatherapp.model.MyAlert
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class Dialoge(private val contextFrag: Context, val listner : SaveAlertInterface): DialogFragment() {
    private lateinit var dialog: AlertDialog
    private lateinit var mTimePicker: TimePickerDialog
    private var myAlert = MyAlert("",0,0,0,0,"","")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val bindingDialog = DataBindingUtil.inflate<AddAlertDialogBinding>(
            LayoutInflater.from(context),
            R.layout.add_alert_dialog,
            null,
            false
        )
        builder.setView(bindingDialog.root)

        //close alert
        bindingDialog.ivClose.setOnClickListener { dialog.dismiss() }

        //start date ........................................
        bindingDialog.tvStartDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                contextFrag,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    bindingDialog.tvStartDate.text = "$dayOfMonth/${monthOfYear+1}/$year"
                    val calendar = Calendar.getInstance()
                    calendar.set(year,monthOfYear, dayOfMonth)
                    myAlert.startDate = calendar.timeInMillis
                    Log.e("dialoge","startDate->"+ myAlert.startDate )
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.timeInMillis = myAlert.startDate
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val selectedDate = dateFormat.format(selectedCalendar.time)
                    Log.e("dialoge","startDate after ->"+ selectedDate)
                    val selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH)
                    val selectedMonth = selectedCalendar.get(Calendar.MONTH) + 1
                    val selectedYear = selectedCalendar.get(Calendar.YEAR)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        //end date ........................................
        bindingDialog.tvEndDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                contextFrag,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    bindingDialog.tvEndDate.text = "$dayOfMonth/${monthOfYear+1}/$year"
                    val calendar = Calendar.getInstance()
                    calendar.set(year,monthOfYear, dayOfMonth)
                    myAlert?.endDate = calendar.timeInMillis
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        //start time ........................................
        bindingDialog.tvStartTime.setOnClickListener {
            val (hour, minute) = showTimePicker()
            mTimePicker = TimePickerDialog(
                contextFrag, R.style.MyTimePickerDialogTheme,
                { view, hourOfDay, minute ->
                    bindingDialog.tvStartTime.text = String.format("%d : %d ", hourOfDay, minute)
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    myAlert.startTime =calendar.timeInMillis
                }, hour, minute, false
            )
            bindingDialog.tvStartTime.setOnClickListener {
                mTimePicker.show()
            }
        }

        //end time ........................................
        bindingDialog.tvEndTime.setOnClickListener {
            val (hour, minute) = showTimePicker()
            mTimePicker = TimePickerDialog(
                contextFrag, R.style.MyTimePickerDialogTheme,
                { view, hourOfDay, minute ->
                    bindingDialog.tvEndTime.text = String.format("%d : %d", hourOfDay, minute)
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    myAlert.endTime =calendar.timeInMillis

                }, hour, minute, false
            )
            bindingDialog.tvEndTime.setOnClickListener {
                mTimePicker.show()
            }
        }

        //event spinner ........................................
        bindingDialog.eventSpinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem: String = parent?.getItemAtPosition(position).toString()
                myAlert.event=selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //sound spinner ........................................
        bindingDialog.soundSpinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem: String = parent?.getItemAtPosition(position).toString()
                myAlert.type=selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //save alert
        bindingDialog.addAlertBtn.setOnClickListener {
            if (bindingDialog.tvDescription.text.toString().isEmpty() || bindingDialog.tvStartDate.text.toString().isEmpty()|| bindingDialog.tvEndDate.text.toString().isEmpty()|| bindingDialog.tvStartTime.text.toString().isEmpty() || bindingDialog.tvEndTime.text.toString().isEmpty()){
                val snackbar =  Snackbar.make(
                    bindingDialog.root, R.string.complete,
                    Snackbar.LENGTH_LONG
                )
                snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_navy))
                snackbar.setTextColor(Color.WHITE)
                snackbar.show()
            }

            else if (myAlert.startDate >=myAlert.endDate || myAlert.startDate<Calendar.getInstance().timeInMillis){
                val snackbar =  Snackbar.make(
                    bindingDialog.root, R.string.datInterval,
                    Snackbar.LENGTH_LONG
                )
                snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_navy))
                snackbar.setTextColor(Color.WHITE)
                snackbar.show()

            }
                else if (myAlert.startTime > myAlert.endTime){
                val snackbar =  Snackbar.make(
                    bindingDialog.root, R.string.timeInterval,
                    Snackbar.LENGTH_LONG
                )
                snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_navy))
                snackbar.setTextColor(Color.WHITE)
                snackbar.show()

            }
                else if (myAlert.startDate==Calendar.getInstance().timeInMillis && myAlert.startTime< Calendar.getInstance().timeInMillis ){
                val snackbar =  Snackbar.make(
                    bindingDialog.root, R.string.timePassed,
                    Snackbar.LENGTH_LONG
                )
                snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_navy))
                snackbar.setTextColor(Color.WHITE)
                snackbar.show()
            }

                else{
                    myAlert.description= bindingDialog.tvDescription.text.toString()
                    listner.onClickSave(myAlert)
                    dialog.dismiss()
                }

        }

        dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }


    private fun showTimePicker(): Pair<Int, Int> {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        return Pair(hour, minute)
    }


    interface SaveAlertInterface {
        fun onClickSave(myAlert: MyAlert)
    }
}