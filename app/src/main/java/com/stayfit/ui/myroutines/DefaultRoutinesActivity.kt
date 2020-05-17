package com.stayfit.ui.myroutines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.stayfit.R
import com.stayfit.ui.workouts.exercises.Exercise
import kotlinx.android.synthetic.main.activity_default_routines.*
import kotlinx.android.synthetic.main.fragment_my_routines.*

class DefaultRoutinesActivity : AppCompatActivity() {
    lateinit var routinesList: ArrayList<Routine>
    private val TAG = "DefaultRoutinesFragment"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default_routines)
        Log.d(TAG, "onCreateView: MyRoutinesFragment created...")

        //var buttonCalendar: ImageView = view.findViewById(R.id.ic_addToCalendar)
        //buttonCalendar.setOnClickListener { calendarEvent() }
        routinesList = ArrayList()
        showList()
    }
    private fun showList() {
        loadData()
        defaultroutinesRecycler.layoutManager = LinearLayoutManager(this)
        defaultroutinesRecycler.addItemDecoration(DividerItemDecoration(this, 1))
        defaultroutinesRecycler.adapter = DefaultRoutineAdapter(routinesList)
        (defaultroutinesRecycler.adapter as DefaultRoutineAdapter).setOnItemClickListener(object :
            DefaultRoutineAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                startConcreteRoutine(routinesList[position])
            }
            override fun onItemLongClick(position: Int, v: View?) {
            }
        })
    }
    fun startConcreteRoutine(r: Routine) {
        Log.d(TAG, ">>> startConcreteRoutine: $r")
        val intent = Intent(this, RoutineActivity::class.java)
        intent.putExtra("routine_map",r.hashMapExercises);
        startActivity(intent)
    }
    fun loadData(){
        var h: HashMap<String,ArrayList<ArrayList<String>>> = HashMap()
        var exercises:ArrayList<ArrayList<String>> = ArrayList()
        var assistedReverseSideSetup: Exercise = Exercise("Assisted Reverse Side Situp", "https://www.youtube.com/watch?v=6DYCoKCHanI&feature=youtu.be", "30","null","How to: Start lying on left side, resting most of weight on left hip, with legs in the air at a 45-degree angle, and place left forearm on the floor for support. Bend knees as you bring them toward chest, and lift chest to meet them. Lower back to start. That’s one rep. We recommend to do 15 reps on each side.\n" +
        "\n" + "Good for: obliques and transverse abs")
        exercises.add(assistedReverseSideSetup.getParametersList())
        var bentLegVUp: Exercise = Exercise("Bent Leg V-Up","https://www.youtube.com/watch?v=Un7DaREYAWk&feature=youtu.be", "30", "null","How to: Start lying on back with legs in air and bent at 90-degrees (shins parallel to floor) and hands clasped over chest. In one movement, straighten legs and lift torso up, extending arms and trying to touch toes with hands. Lower back down to start. That’s one rep. We recommend to do 15 reps.\n" +
                "\n" +
                "Good for: six-pack abs and transverse abs")
        exercises.add(bentLegVUp.getParametersList())
        h["exercises"] = exercises
        routinesList.add(Routine("Abs like rocks", "Perfect routine for people who wants abs results in short time.",R.drawable.abs_default.toString(),h ))
        var h1: HashMap<String,ArrayList<ArrayList<String>>> = HashMap()
        val list_ex: ArrayList<ArrayList<String>> = ArrayList()
        var parametersExercise2: ArrayList<String> = ArrayList()
        parametersExercise2.add("Run")
        parametersExercise2.add("")
        parametersExercise2.add("1000")
        parametersExercise2.add("null")
        parametersExercise2.add("")
        list_ex.add(parametersExercise2)
        h1["exercises"] = list_ex
        routinesList.add(Routine("Run for life", "Velocity test.",R.drawable.blog_2.toString(),h1 ))
    }

}
