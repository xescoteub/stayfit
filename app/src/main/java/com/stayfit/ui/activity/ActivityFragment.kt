package com.stayfit.ui.activity

import android.content.Intent
import android.os.Bundle
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

class ActivityFragment : Fragment() {

    companion object {
        fun newInstance() = ActivityFragment()
    }

    private lateinit var viewModel: ActivityViewModel
    var theDate: TextView ?= null
    var mCalendarView:android.widget.CalendarView ?= null
    lateinit var events:MutableMap<String,ArrayList<Routine>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view:View = inflater.inflate(R.layout.calendar_fragment, container, false)

        mCalendarView = view.findViewById(R.id.calendarView)

        var date:String = SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(Date())
        date = date + " EVENTS :"
        theDate=view.findViewById(R.id.datePicker)
        theDate!!.setText("" + date)
        if(!events.containsKey(date)){
            Toast.makeText(activity,"No events", Toast.LENGTH_SHORT).show()
        }else{

            //showList(date)
        }

        mCalendarView!!.setOnDateChangeListener(CalendarView.OnDateChangeListener { calendarView, year, month, dayOfMonth ->
            var date:String = dayOfMonth.toString() + "-" + "0" + (month + 1) + "-" + year + " EVENTS :"
            theDate=view.findViewById(R.id.datePicker)
            theDate!!.setText("" + date)
            if(!events.containsKey(date)){
                Toast.makeText(activity,"No events", Toast.LENGTH_SHORT).show()
            }else{
               //showList(date)
            }
        })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ActivityViewModel::class.java)
        events = mutableMapOf()
        // TODO: Use the ViewModel
    }

    fun addEvent(year:String,month:String,dayOfMonth:String,routine:Routine){
        val dateEvent:String = dayOfMonth + "-" + "0" + month + "-" + year + " EVENTS :"
        if(!events.containsKey(dateEvent)){
            events.put(dateEvent,arrayListOf(routine))
        }else{
            var list:ArrayList<Routine> = events[dateEvent]!!
            list.add(routine)
            events.put(dateEvent,list)
        }

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

