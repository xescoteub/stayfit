package com.stayfit.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.stayfit.R
import com.stayfit.ui.myroutines.Routine
import java.text.SimpleDateFormat
import java.util.*

class ActivityFragment : Fragment() {

    companion object {
        fun newInstance() = ActivityFragment()
    }

    private lateinit var viewModel: ActivityViewModel
    var theDate: TextView ?= null
    var mCalendarView:android.widget.CalendarView ?= null
    var events:MutableMap<Routine,String> = mutableMapOf()

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

        mCalendarView!!.setOnDateChangeListener(CalendarView.OnDateChangeListener { calendarView, year, month, dayOfMonth ->
            var date:String = dayOfMonth.toString() + "-" + "0" + (month + 1) + "-" + year + " EVENTS :"
            theDate=view.findViewById(R.id.datePicker)
            theDate!!.setText("" + date)
        })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ActivityViewModel::class.java)
        // TODO: Use the ViewModel
    }
    
    fun addEvent(year:Int,month:Int,dayOfMonth:Int,routine:Routine){
        val dateEvent:String = dayOfMonth.toString() + "-" + "0" + (month + 1) + "-" + year + " EVENTS :"
        events.put(routine,dateEvent)
    }
}

