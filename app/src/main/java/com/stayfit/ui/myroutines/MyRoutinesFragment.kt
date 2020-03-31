package com.stayfit.ui.myroutines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.stayfit.R
import kotlinx.android.synthetic.main.fragment_my_routines.*

class MyRoutinesFragment: Fragment() {
    lateinit var routinesList: ArrayList<Routine>
    companion object {
        fun newInstance() = MyRoutinesFragment()
    }
    private val TAG = "MyRoutinesFragment"
    private lateinit var viewModel: MyRoutinesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: MyRoutinesFragment created...")
        return inflater.inflate(R.layout.fragment_my_routines, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyRoutinesViewModel::class.java)
        routinesList = ArrayList()
        addRoutines("Rutina de pechito")
        showList()
    }
    private fun addRoutines(nameRutine: String) {
        routinesList.add(Routine(nameRutine,"", R.drawable.blog_5) )
    }
    private fun showList() {
        myroutinesRecycler.layoutManager = LinearLayoutManager(activity)
        myroutinesRecycler.addItemDecoration(DividerItemDecoration(activity, 1))
        myroutinesRecycler.adapter = RoutineAdapter(routinesList)
        (myroutinesRecycler.adapter as RoutineAdapter).setOnItemClickListener(object :
            RoutineAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                startConcreteRoutine(routinesList[position].name)
            }
            override fun onItemLongClick(position: Int, v: View?) {
            }
        })
    }

    fun startConcreteRoutine(s: String){
    }
}