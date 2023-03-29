package com.example.myweatherapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.myweatherapp.databinding.ConfirmDialogBinding


class ConfirmDialogFragment(var listner : ConfirmDeleteInterface): AppCompatDialogFragment(){
    private lateinit var builder: AlertDialog.Builder
    private lateinit var bindingDialog: ConfirmDialogBinding
    private lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingDialog =
            ConfirmDialogBinding.inflate(LayoutInflater.from(context), container, false)

bindingDialog.cancelBtn.setOnClickListener {
    dialog.dismiss()
}
        bindingDialog.deleteBtn.setOnClickListener {
            listner.onClick(true)
            dialog.dismiss()
        }
        builder = AlertDialog.Builder(requireContext())
        builder.setView(bindingDialog.root)
        dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}

interface ConfirmDeleteInterface {
    fun onClick(confirmDelete:Boolean)
}