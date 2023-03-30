package com.example.myweatherapp.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.ConfirmDialogBinding


class ConfirmDialogFragment(var listner : ConfirmDeleteInterface): AppCompatDialogFragment(){

    private lateinit var dialog: AlertDialog


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val binding = DataBindingUtil.inflate<ConfirmDialogBinding>(
            LayoutInflater.from(context),
            R.layout.confirm_dialog,
            null,
            false
        )
        builder.setView(binding.root)
        dialog = builder.create()

        binding.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        binding.deleteBtn.setOnClickListener {
            listner.onClick(true)
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
        }


interface ConfirmDeleteInterface {
    fun onClick(confirmDelete:Boolean)
}