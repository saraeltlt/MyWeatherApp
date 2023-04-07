package com.example.myweatherapp.notifications.notificationview

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.AddAlertDialogBinding
import com.example.myweatherapp.model.MyAlert
import com.google.android.material.snackbar.Snackbar
import java.util.*


class NotificationsDialog(private val contextFrag: Context, val listner : SaveAlertInterface): DialogFragment() {
    private lateinit var dialog: AlertDialog
    val calendarToday = Calendar.getInstance()
    val yearToday = calendarToday.get(Calendar.YEAR)
    val monthToday = calendarToday.get(Calendar.MONTH)
    val dayToday = calendarToday.get(Calendar.DAY_OF_MONTH)
    val hourToday = calendarToday.get(Calendar.HOUR_OF_DAY)
    val minuteToday = calendarToday.get(Calendar.MINUTE)
    val calendarFrom = Calendar.getInstance()
    val calendarTo = Calendar.getInstance()
    private var myAlert = MyAlert("",0,0,"","")
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

            val datePickerDialog = DatePickerDialog(
                contextFrag,
                DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    bindingDialog.tvStartDate.text = "$day/${month+1}/$year"
                    calendarFrom.set(year,month, day)

                },
                yearToday,
                monthToday,
                dayToday
            )
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
            datePickerDialog.show()
        }

        //end date ........................................
        bindingDialog.tvEndDate.setOnClickListener {

            val datePickerDialog = DatePickerDialog(
                contextFrag,
                DatePickerDialog.OnDateSetListener { view, year, month, day->
                    bindingDialog.tvEndDate.text = "$day/${month+1}/$year"
                    calendarTo.set(year,month, day)
                },
                yearToday,
                monthToday,
                dayToday
            )
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
            datePickerDialog.show()
        }

        //start time ........................................
        bindingDialog.tvStartTime.setOnClickListener {
            val mTimePicker = TimePickerDialog(
                contextFrag, R.style.MyTimePickerDialogTheme,
                { view, hour, minute ->
                    bindingDialog.tvStartTime.text = String.format("%d : %d ", hour, minute)
                    calendarFrom.set(Calendar.HOUR_OF_DAY, hour)
                    calendarFrom.set(Calendar.MINUTE, minute)
                    calendarFrom.set(Calendar.SECOND, 0)
                    calendarFrom.set(Calendar.MILLISECOND, 0)
                }, hourToday, minuteToday, false
            )
            bindingDialog.tvStartTime.setOnClickListener {
                mTimePicker.show()
            }
        }

        //end time ........................................
        bindingDialog.tvEndTime.setOnClickListener {
            val mTimePicker = TimePickerDialog(
                contextFrag, R.style.MyTimePickerDialogTheme,
                { view, hour, minute ->
                    bindingDialog.tvEndTime.text = String.format("%d : %d", hour, minute)
                    calendarTo.set(Calendar.HOUR_OF_DAY, hour)
                    calendarTo.set(Calendar.MINUTE, minute)
                    calendarTo.set(Calendar.SECOND, 0)
                    calendarTo.set(Calendar.MILLISECOND, 0)

                }, hourToday, minuteToday, false
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
                 if (selectedItem== context?.getResources()?.getString(R.string.rain)){
                    myAlert.event="rain"
                }
               else if (selectedItem== context?.getResources()?.getString(R.string.storm)){
                    myAlert.event="storm"
                }
                else if (selectedItem== context?.getResources()?.getString(R.string.heat_advisory)){
                    myAlert.event="heat advisory"
                }
                else if (selectedItem== context?.getResources()?.getString(R.string.heat)){
                    myAlert.event="heat"
                }

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
                if (selectedItem== context?.getResources()?.getString(R.string.notifications)){
                        myAlert.type="n"
                    }
                else if (selectedItem== context?.getResources()?.getString(R.string.alert)){
                    myAlert.type="a"
                }


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

            /*else if (calendarFrom.timeInMillis >=calendarTo.timeInMillis || calendarFrom.timeInMillis<calendarToday.timeInMillis){

                val snackbar =  Snackbar.make(
                    bindingDialog.root, R.string.timeInterval,
                    Snackbar.LENGTH_LONG
                )
                snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_navy))
                snackbar.setTextColor(Color.WHITE)
                snackbar.show()

            }*/

                else{
                    myAlert.description= bindingDialog.tvDescription.text.toString()
                    myAlert.start=calendarFrom.timeInMillis
                     myAlert.end=calendarTo.timeInMillis
                    listner.onClickSave(myAlert)
                    dialog.dismiss()
               }

        }

        dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    interface SaveAlertInterface {
        fun onClickSave(myAlert: MyAlert)
    }
}