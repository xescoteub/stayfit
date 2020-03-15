package com.stayfit.ui.workouts

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.stayfit.R

class WorkoutsFragment : Fragment() {

    companion object {
        fun newInstance() = WorkoutsFragment()
    }

    private lateinit var viewModel: WorkoutsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.workouts_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WorkoutsViewModel::class.java)
        // TODO: Use the ViewModel
    }
    fun startArmMenu(view: View) {
        //val intent = Intent(this, ArmMenu::class.java)
        //startActivity(intent)
    }
}
