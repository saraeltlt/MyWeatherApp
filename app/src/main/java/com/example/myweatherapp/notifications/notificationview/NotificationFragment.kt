package com.example.myweatherapp.notifications.notificationview

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.AddAlertDialogBinding
import com.example.myweatherapp.databinding.FragmentNotificationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView




class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var builder: AlertDialog.Builder
    private lateinit var bindingDialog: AddAlertDialogBinding
    private lateinit var dialog: Dialoge

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
        bindingDialog =
            AddAlertDialogBinding.inflate(LayoutInflater.from(context),container , false)
        binding.lifecycleOwner=this
        val view = binding.root
        binding.notifFab.setOnClickListener {
            showAlertDialog()
        }
        bindingDialog.ivClose.setOnClickListener { dialog.dismiss() }



        return view
    }
 fun   showAlertDialog(){
     dialog= Dialoge(requireContext())
     dialog.show(parentFragmentManager, "alertDialog")


 }

}