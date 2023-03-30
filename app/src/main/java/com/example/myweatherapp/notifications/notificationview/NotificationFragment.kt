package com.example.myweatherapp.notifications.notificationview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.myweatherapp.utils.MyApp
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentNotificationBinding
import com.example.myweatherapp.model.MyAlert
import com.example.myweatherapp.notifications.notificationviewmodel.NotificationViewModel
import com.example.myweatherapp.notifications.notificationviewmodel.NotificationViewModelFactory
import com.example.myweatherapp.ui.ConfirmDeleteInterface
import com.example.myweatherapp.ui.ConfirmDialogFragment
import com.google.android.material.bottomnavigation.BottomNavigationView




class NotificationFragment : Fragment() , OnNotifClickListner, Dialoge.SaveAlertInterface,
    ConfirmDeleteInterface {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var dialog: Dialoge
    lateinit var viewModel: NotificationViewModel
    lateinit var factory: NotificationViewModelFactory
    lateinit var adapter: NotificationAdapter

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
 fun   showAlertDialog(){
     dialog= Dialoge(requireContext(),this)
     dialog.show(parentFragmentManager, "alertDialog")

 }

    override fun onDeleteClick(myAlert: MyAlert) {
     //   myalert=alert
        var dialog = ConfirmDialogFragment(this)
        dialog.show(childFragmentManager, "alertDialog")
    }

    override fun onClickSave() {
        //input alert
        // viewModel.SaveAlert()
    }

    override fun onClick(confirmDelete: Boolean) {
        if (confirmDelete) {
           // viewModel.deleteAlert(myAlert)
            Toast.makeText(requireContext(),R.string.removedFromAlerts, Toast.LENGTH_LONG).show()
        }
    }

}