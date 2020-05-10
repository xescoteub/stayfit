package com.stayfit.ui.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.stayfit.R
import com.stayfit.ui.myroutines.Routine
import com.stayfit.ui.myroutines.RoutineActivity
import com.stayfit.ui.myroutines.RoutineAdapter
import com.stayfit.ui.myroutines.RoutineAdapterCalendar
import kotlinx.android.synthetic.main.calendar_fragment.*
import kotlinx.android.synthetic.main.fragment_my_routines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ActivityFragment : Fragment() {

    companion object {
        fun newInstance() = ActivityFragment()
    }

    private val TAG = "CalendarActivity"

    private lateinit var viewModel: ActivityViewModel
    var theDate: TextView ?= null
    var mCalendarView:android.widget.CalendarView ?= null
    private lateinit var mAuth: FirebaseAuth

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view:View = inflater.inflate(R.layout.calendar_fragment, container, false)
        Log.d(TAG, "onCreateView: CalendarAcrivity created...")
        //mAuth = FirebaseAuth.getInstance()

        dayPicker(view)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ActivityViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun dayPicker(view:View){
        mCalendarView = view.findViewById(R.id.calendarView)

        var date: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        date = date + " EVENTS :"
        theDate = view.findViewById(R.id.datePicker)
        theDate!!.setText("" + date)
        /*if (!events.containsKey(date)) {
            Toast.makeText(activity, "No events", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "Events avaliable", Toast.LENGTH_SHORT).show()
            //showList(date)
        }*/

        mCalendarView!!.setOnDateChangeListener(CalendarView.OnDateChangeListener { calendarView, year, month, dayOfMonth ->
            if (dayOfMonth.toString().length == 1) {
                if (month.toString().length == 1) {
                    var date: String =
                        "0" + dayOfMonth.toString() + "-" + "0" + (month + 1) + "-" + year + " EVENTS :"
                    theDate = view.findViewById(R.id.datePicker)
                    theDate!!.setText("" + date)
                    /*if (!events.containsKey(date)) {
                        Toast.makeText(activity, "No events", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "Events avaliable", Toast.LENGTH_SHORT).show()
                        //showList(date)
                    }*/
                } else {
                    var date: String =
                        "0" + dayOfMonth.toString() + "-" + (month + 1) + "-" + year + " EVENTS :"
                    theDate = view.findViewById(R.id.datePicker)
                    theDate!!.setText("" + date)
                    /*if (!events.containsKey(date)) {
                        Toast.makeText(activity, "No events", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "Events avaliable", Toast.LENGTH_SHORT).show()
                        //showList(date)
                    }*/
                }
            } else {
                if (month.toString().length == 1) {
                    var date: String =
                        dayOfMonth.toString() + "-" + "0" + (month + 1) + "-" + year + " EVENTS :"
                    theDate = view.findViewById(R.id.datePicker)
                    theDate!!.setText("" + date)
                    /*if (!events.containsKey(date)) {
                        Toast.makeText(activity, "No events", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "Events avaliable", Toast.LENGTH_SHORT).show()
                        //showList(date)
                    }*/
                } else {
                    var date: String =
                        dayOfMonth.toString() + "-" + (month + 1) + "-" + year + " EVENTS :"
                    theDate = view.findViewById(R.id.datePicker)
                    theDate!!.setText("" + date)
                    /*if (!events.containsKey(date)) {
                        Toast.makeText(activity, "No events", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "Events avaliable", Toast.LENGTH_SHORT).show()
                        //showList(date)
                    }*/
                }
            }
        })

    }


    /*private fun showList(eventDay:String) {
        recyclerCalendar.layoutManager = LinearLayoutManager(activity)
        var list:ArrayList<Routine> = events[eventDay]!!
        recyclerCalendar.adapter = RoutineAdapterCalendar(list)
        (recyclerCalendar.adapter as RoutineAdapterCalendar).setOnItemClickListener(object :
            RoutineAdapterCalendar.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                //startConcreteRoutine(list[position])
            }
            override fun onItemLongClick(position: Int, v: View?) {
            }
        })
    }*/

}

