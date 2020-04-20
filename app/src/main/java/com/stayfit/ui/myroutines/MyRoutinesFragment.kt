package com.stayfit.ui.myroutines

import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stayfit.R
import com.stayfit.ui.activity.ActivityFragment
import com.stayfit.ui.workouts.exercises.Exercise
import kotlinx.android.synthetic.main.fragment_my_routines.*
import kotlinx.android.synthetic.main.routine_change_time.*
import java.lang.reflect.Type


class   MyRoutinesFragment: Fragment(){
    lateinit var routinesList: ArrayList<Routine>
    var routineSelected:Routine ?= null
    var calendarEvents:ActivityFragment ?= null

    companion object {
        fun newInstance() = MyRoutinesFragment()
    }
    private val TAG = "MyRoutinesFragment"
    private lateinit var viewModel: MyRoutinesViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view:View = inflater.inflate(R.layout.fragment_my_routines, container, false)
        Log.d(TAG, "onCreateView: MyRoutinesFragment created...")

        var button: ImageView = view.findViewById(R.id.ic_deleteRoutine)
        button.setOnClickListener { deleteRoutine() }

        var buttonAdd: ImageView = view.findViewById(R.id.ic_addRoutine)
        buttonAdd.setOnClickListener { newRoutine(view) }

        var buttonCalendar: ImageView = view.findViewById(R.id.ic_addToCalendar)
        buttonCalendar.setOnClickListener { calendarEvent() }

        return view
    }

    private fun calendarEvent() {
        myroutinesRecycler.adapter = RoutineAdapter(routinesList)
        (myroutinesRecycler.adapter as RoutineAdapter).setOnItemClickListener(object :
            RoutineAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                val adb: AlertDialog.Builder = AlertDialog.Builder(activity)

                val mView:View = getLayoutInflater().inflate(R.layout.layout_dialog_calendar,null)
                val day: EditText = mView.findViewById(R.id.edit_day)
                val month: EditText = mView.findViewById(R.id.edit_month)
                val year: EditText = mView.findViewById(R.id.edit_year)

                adb.setNegativeButton("Cancel", null)
                adb.setPositiveButton("Ok") { dialog, which ->
                    calendarEvents?.addEvent(year.toString(),month.toString() ,day.toString(),routinesList.get(position))
                    Toast.makeText(activity,"Event added",Toast.LENGTH_SHORT).show()
                }
                adb.setTitle("Select a day to set the event")
                adb.setView(mView)
                val dialog:AlertDialog =adb.create()
                dialog.show()
            }

            override fun onItemLongClick(position: Int, v: View?) {

            }
        })


    }

    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var button_delete: ImageButton = getView()!!.findViewById(R.id.ic_deleteRoutine)
    } */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyRoutinesViewModel::class.java)
        routinesList = ArrayList()
        showList()
    }
    private fun showList() {
        try {
            loadData()
        }catch (e: Exception){
        }
        myroutinesRecycler.layoutManager = LinearLayoutManager(activity)
        myroutinesRecycler.addItemDecoration(DividerItemDecoration(activity, 1))
        myroutinesRecycler.adapter = RoutineAdapter(routinesList)
        (myroutinesRecycler.adapter as RoutineAdapter).setOnItemClickListener(object :
            RoutineAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                startConcreteRoutine(routinesList[position])
            }
            override fun onItemLongClick(position: Int, v: View?) {
            }
        })
    }

    fun startConcreteRoutine(r: Routine){
        val intent = Intent(activity, RoutineActivity::class.java)
        intent.putParcelableArrayListExtra("routine_list",r.getExercisesList());
        startActivity(intent)
    }
    private fun saveData(){
        var sharedPreferences: SharedPreferences = this.activity!!.getSharedPreferences("shared preferences", MODE_PRIVATE)
        var editor: SharedPreferences.Editor = sharedPreferences.edit()
        var gson: Gson = Gson()
        var jsonRoutines: String = gson.toJson(routinesList)
        editor.putString("routines list", jsonRoutines)
        editor.apply()
    }
    private fun loadData(){
        var sharedPreferences: SharedPreferences = this.activity!!.getSharedPreferences("shared preferences", MODE_PRIVATE)
        var gson: Gson = Gson()
        var jsonRoutines: String = sharedPreferences.getString("routines list", null)
        val typeRoutine: Type = object : TypeToken<ArrayList<Routine?>?>() {}.type
        routinesList = gson.fromJson(jsonRoutines,typeRoutine)
        if (routinesList == null) {
            routinesList = ArrayList()
        }
    }
    private fun deleteRoutine() {
        myroutinesRecycler.adapter = RoutineAdapter(routinesList)
        (myroutinesRecycler.adapter as RoutineAdapter).setOnItemClickListener(object :
            RoutineAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                val adb: AlertDialog.Builder = AlertDialog.Builder(activity)
                adb.setTitle("Delete?")
                adb.setMessage("Are you sure you want to delete ${routinesList.get(position).name}?")
                adb.setNegativeButton("Cancel", null)
                adb.setPositiveButton("Ok") { dialog, which ->
                    routinesList.removeAt(position)
                    saveData()
                    showList()
                }
                adb.show()
            }
            override fun onItemLongClick(position: Int, v: View?) {

            }
        })
    }
    private fun addRoutine(r: Routine){
        routinesList.add(r)
        saveData()
    }

    private fun newRoutine(view: View) {
        val intent = Intent(activity, FormRoutine::class.java)
        startActivityForResult(intent, 3);// Activity is started with requestCode 2
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // https://developer.android.com/training/basics/intents/result  https://stackoverflow.com/questions/19666572/how-to-call-a-method-in-another-activity-from-activity
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3) {
            if (resultCode == 3){
                var arrayList: ArrayList<String> = data!!.getStringArrayListExtra("LIST ROUTINE")
                if (!arrayList[2].equals("null")){addRoutine(Routine( arrayList[0], arrayList[1], MediaStore.Images.Media.getBitmap(activity!!.contentResolver, arrayList[2].toUri()), ArrayList()))}
                else{addRoutine(Routine( arrayList[0], arrayList[1], null, ArrayList()))}

                showList()
            }
        }
    }
    fun findRoutineByName(name: String): Routine{
        var routine:Routine ?= null
        for (r in routinesList){
            if (name.equals(r.getNameRoutine())){
                routine = r
            }
        }
        return routine!!
    }



}