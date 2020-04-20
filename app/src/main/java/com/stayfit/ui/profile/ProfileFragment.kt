package com.stayfit.ui.profile

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.stayfit.R

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        var view:View = inflater.inflate(R.layout.profile_fragment, container, false)

        var instabutton: ImageView = view.findViewById(R.id.instaButton)
        instabutton.setOnClickListener { change_psswd_OnClick() }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun change_psswd_OnClick(){

        val psswdDialog = AlertDialog.Builder(activity)
        psswdDialog.setView(R.layout.profile_change_psswd)
        psswdDialog.setPositiveButton("Yes") { dialog, which ->
            //Actualizar contraseña
        }
        psswdDialog.setNegativeButton("No") { dialog, which -> }
        psswdDialog.show()
    }




}
