package com.stayfit.ui.activity

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.stayfit.ui.workouts.exercises.Exercise
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
    var exercisesRoutine: ArrayList<ArrayList<Exercise>> ?= null
    lateinit var routinesListC: ArrayList<Routine>
    var currentDayEvent:String ?= null
    var eventsAvailable:Boolean ?= null
    var progressBar:ProgressBar ?= null

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view:View = inflater.inflate(R.layout.calendar_fragment, container, false)
        Log.d(TAG, "onCreateView: CalendarAcrivity created...")

        mAuth = FirebaseAuth.getInstance()

        progressBar = view.findViewById(R.id.waitingBar)
        progressBar!!.visibility = View.GONE

        var buttonDelete: ImageView = view.findViewById(R.id.ic_deleteEvent)
        buttonDelete.setOnClickListener { deleteRoutine() }

        dayPicker(view)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ActivityViewModel::class.java)
        exercisesRoutine = ArrayList()
        routinesListC = ArrayList()
    }

    fun dayPicker(view:View){
        mCalendarView = view.findViewById(R.id.calendarView)

        var date: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        theDate = view.findViewById(R.id.datePicker)
        theDate!!.setText("" + date + " EVENTS :")
        //Comprobar si hay eventos
        currentDayEvent=date
        checkForEvents(date)

        mCalendarView!!.setOnDateChangeListener(CalendarView.OnDateChangeListener { calendarView, year, month, dayOfMonth ->
            if (dayOfMonth.toString().length == 1) {
                if (month.toString().length == 1) {
                    var date: String = "0" + dayOfMonth.toString() + "-" + "0" + (month + 1) + "-" + year
                    theDate = view.findViewById(R.id.datePicker)
                    theDate!!.setText("" + date + " EVENTS :")
                    //Comprobar si hay eventos
                    currentDayEvent=date
                    checkForEvents(date)

                } else {
                    var date: String = "0" + dayOfMonth.toString() + "-" + (month + 1) + "-" + year
                    theDate = view.findViewById(R.id.datePicker)
                    theDate!!.setText("" + date + " EVENTS :")
                    //Comprobar si hay eventos
                    currentDayEvent=date
                    checkForEvents(date)

                }
            } else {
                if (month.toString().length == 1) {
                    var date: String = dayOfMonth.toString() + "-" + "0" + (month + 1) + "-" + year
                    theDate = view.findViewById(R.id.datePicker)
                    theDate!!.setText("" + date + " EVENTS :")
                    //Comprobar si hay eventos
                    currentDayEvent=date
                    checkForEvents(date)

                } else {
                    var date: String = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
                    theDate = view.findViewById(R.id.datePicker)
                    theDate!!.setText("" + date + " EVENTS :")
                    //Comprobar si hay eventos
                    currentDayEvent=date
                    checkForEvents(date)
                }
            }
        })

    }

    private fun checkForEvents(date:String){
        progressBar!!.visibility = View.VISIBLE
        var arrayAdapter = activity?.let {
            ArrayAdapter<ArrayList<Exercise>>(
                it,
                android.R.layout.select_dialog_singlechoice
            )
        }

        val currentUserID = mAuth.currentUser?.uid.toString()

        db.collection("events").get()
            .addOnSuccessListener { result ->
                var userFound = false
                for(user in result){
                    if(user.id.equals(currentUserID)){
                        userFound=true
                    }
                }
                if(userFound){
                    db.collection("events").document(currentUserID).collection("myEvents").get()
                        .addOnSuccessListener { result2 ->
                            var dateEventFound = false
                            for(dayEvent in result2){
                                if(dayEvent.id.equals(date)){
                                    dateEventFound = true
                                }
                            }
                            if(dateEventFound){
                                db.collection("events").document(currentUserID).collection("myEvents").document(date).collection("myRoutines").get()
                                    .addOnSuccessListener { result3 ->
                                        //Contador para saber si hay rutinas prueba
                                        var i = 0
                                        var routinesList:ArrayList<Routine> = arrayListOf()
                                        for(routinesR in result3){
                                            Log.d(TAG, "${routinesR.id} => ${routinesR.data}")
                                            val routineObj = routinesR.data as HashMap<*, *>
                                            val routine = Routine()
                                            var h: HashMap<String,ArrayList<ArrayList<String>>> = HashMap()

                                            with(routine) {
                                                name    = routineObj["name"].toString()
                                                description  = routineObj["description"].toString()
                                                photo   = routineObj["photo"].toString()
                                                hashMapExercises  = h
                                                //exercisesRoutine!!.add(routineObj["hashMapExercises"] as ArrayList<Exercise>)
                                                //Log.d(TAG, "Get exs[0] ${routineObj["hashMapExercises"]!!.javaClass}")
                                                Log.d(TAG, "Get exs[0] ${routineObj["hashMapExercises"]}")
                                                var a:ArrayList<HashMap<*,String>> = ArrayList()
                                                if (routineObj["hashMapExercises"] != null){ a= routineObj["hashMapExercises"] as ArrayList<HashMap<*,String>>}
                                                arrayAdapter!!.add(toArrayListExercise2(a))
                                                //Log.d(TAG, "exercisesList: ${routineObj["hashMapExercises"] as ArrayList<Exercise>}")
                                                Log.d(TAG, "routine: $routine")
                                                time_be = routineObj["time_be"].toString()
                                                routinesList.add(routine)
                                            }
                                            Log.d(TAG, "routineList: $routinesList")
                                            i++
                                        }
                                        if(i>0 && arrayAdapter != null){
                                            Log.d(TAG,"Events available")
                                            routinesListC = arrayListOf()
                                            routinesList = loadExercises(routinesList,arrayAdapter)
                                            routinesListC=routinesList
                                            eventsAvailable=true
                                            showList(routinesList)
                                            recyclerCalendar.visibility =View.VISIBLE
                                            progressBar!!.visibility = View.GONE
                                            Toast.makeText(activity,"Events available",Toast.LENGTH_SHORT).show()
                                        }else{
                                            Log.d(TAG,"No events")
                                            progressBar!!.visibility = View.GONE
                                            eventsAvailable=false
                                            recyclerCalendar.visibility =View.GONE
                                            Toast.makeText(activity,"No events",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener{ e3 ->
                                        Log.w(TAG,"Error getting getting data",e3)
                                    }
                            }else{
                                Log.d(TAG,"No events")
                                progressBar!!.visibility = View.GONE
                                eventsAvailable=false
                                recyclerCalendar.visibility =View.GONE
                                Toast.makeText(activity,"No events",Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener{ e2 ->
                            Log.w(TAG,"Error getting getting data",e2)
                        }
                }else{
                    Log.d(TAG,"No events")
                    progressBar!!.visibility = View.GONE
                    eventsAvailable=false
                    recyclerCalendar.visibility =View.GONE
                    Toast.makeText(activity,"No events",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{ e ->
                Log.w(TAG,"Error getting getting data",e)
            }
    }



    private fun showList(routinesList:ArrayList<Routine>) {

        viewModel.setRoutines(getRoutinesNamesList(routinesList))
        recyclerCalendar.layoutManager = LinearLayoutManager(activity)
        recyclerCalendar.addItemDecoration(DividerItemDecoration(activity, 1))
        recyclerCalendar.adapter = RoutineAdapterCalendar(routinesList)
        (recyclerCalendar.adapter as RoutineAdapterCalendar).setOnItemClickListener(object :
            RoutineAdapterCalendar.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                startConcreteRoutine(routinesList[position])
            }
            override fun onItemLongClick(position: Int, v: View?) {
            }
        })
    }

    fun startConcreteRoutine(r: Routine){
        Log.d(TAG, ">>> startConcreteRoutine: $r")
        val intent = Intent(activity, RoutineActivity::class.java)
        if(r.name != "Run for life" && r.name != "Abs like rocks") {
            intent.putExtra("routine_name", r.name)
        }else{
            intent.putExtra("routine_name", "")
        }
        intent.putExtra("routine_map",r.hashMapExercises)
        startActivity(intent)
    }

    fun getRoutinesNamesList(routinesList:ArrayList<Routine>): ArrayList<String>{
        var namesRoutines: ArrayList<String> = ArrayList()
        for (r in routinesList){ namesRoutines.add(r.name)}
        return namesRoutines
    }

    private fun deleteRoutine() {
        if(eventsAvailable!!) {
            Toast.makeText(activity, "Press the routine that you want to delete from this day", Toast.LENGTH_SHORT).show()
            recyclerCalendar.adapter = RoutineAdapterCalendar(routinesListC)
            (recyclerCalendar.adapter as RoutineAdapterCalendar).setOnItemClickListener(object :
                RoutineAdapterCalendar.ClickListener {
                override fun onItemClick(position: Int, v: View?) {
                    val adb: AlertDialog.Builder = AlertDialog.Builder(activity)
                    adb.setTitle("Delete?")
                    adb.setMessage("Are you sure you want to delete ${routinesListC.get(position).name}?")
                    adb.setNegativeButton("Cancel", null)
                    adb.setPositiveButton("Ok") { dialog, which ->
                        deleteItemFireBase(routinesListC.get(position).name)
                        routinesListC.removeAt(position)
                        showList(routinesListC)
                        Log.d(TAG, "END3")
                        recyclerCalendar.adapter!!.notifyDataSetChanged()
                    }
                    adb.show()
                }

                override fun onItemLongClick(position: Int, v: View?) {

                }
            })
        }else{
            Toast.makeText(activity, "No events to delete", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteItemFireBase(exercise_name: String) {
        val currentUserID = mAuth.currentUser?.uid.toString()
        db.collection("events").document(currentUserID).collection("myEvents").document(currentDayEvent!!).collection("myRoutines").document(exercise_name)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    fun toArrayListExercise2(exercises: ArrayList<HashMap<*,String>>): ArrayList<Exercise>{
        var arrayList:ArrayList<Exercise> = ArrayList()
        if (exercises.size>0){
            for (i in exercises.indices) {
                var exercise = exercises[i];
                arrayList.add(
                    Exercise(
                        exercise["nom_exercise"],
                        exercise["url_video"],
                        exercise["time_count"],
                        exercise["jason"],
                        exercise["description"]
                    )
                )
            }
        }

        return arrayList
    }

    private fun loadExercises(routinesList: ArrayList<Routine>, arrayAdapter: ArrayAdapter<ArrayList<Exercise>>): ArrayList<Routine>{ //Devolver el routines list cargado
        for (r in routinesList.indices) {
            Log.d(TAG, "load exercisesList: ${arrayAdapter!!.getItem(r)}")
            var h: HashMap<String,ArrayList<ArrayList<String>>> = HashMap()
            h["exercises"] = arrayListExerciseToArrayListStrings(arrayAdapter!!.getItem(r)!!)
            routinesList[r].hashMapExercises = h
        }
        return routinesList
    }

    private fun arrayListExerciseToArrayListStrings(exercises: ArrayList<Exercise>): ArrayList<ArrayList<String>>{
        var arrayList:ArrayList<ArrayList<String>> = ArrayList()
        Log.d(TAG, "Class ${exercises.javaClass}")
        Log.d(TAG, "EX ${exercises}")
        for (ex in exercises) {
            arrayList.add(ex.getParametersList())
        }
        return arrayList
    }
}