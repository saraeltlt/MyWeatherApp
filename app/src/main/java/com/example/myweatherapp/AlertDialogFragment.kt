package com.example.myweatherapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class AlertDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Alert Dialog Title")
        builder.setMessage("Alert Dialog Message")
        builder.setPositiveButton("OK") { dialog, which ->
            // Do something when the OK button is clicked
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            // Do something when the Cancel button is clicked
        }
        return builder.create()
    }
}
