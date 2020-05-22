package com.stayfit.ui.myroutines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.stayfit.R
import com.stayfit.ui.workouts.exercises.Exercise
import kotlinx.android.synthetic.main.activity_default_routines.*
import kotlinx.android.synthetic.main.fragment_my_routines.*

class DefaultRoutinesActivity : AppCompatActivity() {
    lateinit var routinesList: ArrayList<Routine>
    private val TAG = "DefaultRoutinesFragment"

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default_routines)
        Log.d(TAG, "onCreateView: MyRoutinesFragment created...")

        mAuth = FirebaseAuth.getInstance()

        var buttonCalendar: ImageView = findViewById(R.id.ic_addDRToCalendar)
        buttonCalendar.setOnClickListener { calendarEvent() }

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
        intent.putExtra("routine_name","");
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
        var ex3:Exercise = Exercise("Alternating Toe Reach","https://youtu.be/tzJC-HijGsk", "30", "null","How to: Start lying on back with legs extended in air at 45-degree angle and arms straight out to sides on floor at shoulder level. At the same time, raise right leg up and lift torso trying to touch toes with left hand. Return to start and repeat on the other side. That’s one rep. Do 15 reps.\n" +
                "\n" +
                "Good for: obliques and transverse abs")
        var ex4:Exercise = Exercise("Leg Raise and Reach Clap","https://youtu.be/2O469gGYSew", "30", "null","How to: Start lying on back with legs lifted in air at 45-degree angle and arms by side pressing into mat. Without letting lower back lift off floor, raise legs to hip level while curling upper body off floor and bringing hands to clap behind knees. Return to start. That’s one rep. Do 15 reps.\n" +
                "\n" +
                "Good for: six-pack abs and transverse abs")
        exercises.add(ex3.getParametersList())
        exercises.add(ex4.getParametersList())
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

    private fun calendarEvent() {
        Toast.makeText(this,"Press the routine that you want to set for a day",Toast.LENGTH_SHORT).show()
        defaultroutinesRecycler.adapter = DefaultRoutineAdapter(routinesList)
        ( defaultroutinesRecycler.adapter as DefaultRoutineAdapter).setOnItemClickListener(object :
            DefaultRoutineAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                val adb: AlertDialog.Builder = AlertDialog.Builder(this@DefaultRoutinesActivity)

                val mView:View = getLayoutInflater().inflate(R.layout.layout_dialog_calendar,null)
                val day: EditText = mView.findViewById(R.id.edit_day)
                val month: EditText = mView.findViewById(R.id.edit_month)
                val year: EditText = mView.findViewById(R.id.edit_year)

                adb.setNegativeButton("Cancel", null)
                adb.setPositiveButton("Ok") { dialog, which ->

                    addEvent(year.text.toString(),month.text.toString() ,day.text.toString(),routinesList.get(position))
                }
                adb.setTitle("Select a day to set the event")
                adb.setView(mView)
                val dialog: AlertDialog =adb.create()
                dialog.show()
            }

            override fun onItemLongClick(position: Int, v: View?) {

            }
        })


    }

    fun addEvent(year:String,month:String,dayOfMonth:String,routine:Routine){

        val dateEvent = "$dayOfMonth-$month-$year"


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
                                if(dayEvent.id.equals(dateEvent)){
                                    dateEventFound = true
                                }
                            }
                            if(dateEventFound){
                                db.collection("events").document(currentUserID).collection("myEvents").document(dateEvent).collection("myRoutines").get()
                                    .addOnSuccessListener { result3 ->
                                        var routineAlreadyAdded = false
                                        for(routinesR in result3){
                                            if(routinesR.id.equals(routine.name)){
                                                routineAlreadyAdded = true
                                            }
                                        }
                                        if(routineAlreadyAdded){
                                            Log.d(TAG,"Event already added.")
                                            Toast.makeText(this,"Event ${routine.name} already added",Toast.LENGTH_SHORT).show()
                                        }else{
                                            //Adding User Event Routine
                                            val routineData = hashMapOf(
                                                "name" to routine.name,
                                                "description" to routine.description,
                                                "photo" to routine.photo,
                                                "hashMapExercises" to routine.hashMapExercises?.get("exercises")?.let { toArrayListExercise(it) }
                                            )
                                            db.collection("events").document(currentUserID).collection("myEvents").document(dateEvent).collection("myRoutines").document(routine.name).set(routineData)
                                            Log.d(TAG,"Event added.")
                                            Toast.makeText(this,"Event ${routine.name} added",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener{ e3 ->
                                        Log.w(TAG,"Error getting getting data",e3)
                                    }
                            }else{
                                //Adding User Event day
                                val dataEvent = HashMap<String, String>()
                                dataEvent["event_Day"] = dateEvent
                                db.collection("events").document(currentUserID).collection("myEvents").document(dateEvent).set(dataEvent)
                                //Adding User Event Routine
                                val routineData = hashMapOf(
                                    "name" to routine.name,
                                    "description" to routine.description,
                                    "photo" to routine.photo,
                                    "hashMapExercises" to routine.hashMapExercises?.get("exercises")?.let { toArrayListExercise(it) }
                                )
                                db.collection("events").document(currentUserID).collection("myEvents").document(dateEvent).collection("myRoutines").document(routine.name).set(routineData)
                                Log.d(TAG,"Event added.")
                                Toast.makeText(this,"Event ${routine.name} added",Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener{ e2 ->
                            Log.w(TAG,"Error getting getting data",e2)
                        }
                }else{
                    //Adding User credentials
                    val dataUser = HashMap<String, String>()
                    dataUser["user_Auth"] = mAuth.currentUser?.email.toString()
                    db.collection("events").document(currentUserID).set(dataUser)
                    //Adding User Event day
                    val dataEvent = HashMap<String, String>()
                    dataEvent["event_Day"] = dateEvent
                    db.collection("events").document(currentUserID).collection("myEvents").document(dateEvent).set(dataEvent)
                    //Adding User Event Routine
                    val routineData = hashMapOf(
                        "name" to routine.name,
                        "description" to routine.description,
                        "photo" to routine.photo,
                        "hashMapExercises" to routine.hashMapExercises?.get("exercises")?.let { toArrayListExercise(it) }
                    )
                    db.collection("events").document(currentUserID).collection("myEvents").document(dateEvent).collection("myRoutines").document(routine.name).set(routineData)
                    Log.d(TAG,"Event added.")
                    Toast.makeText(this,"Event ${routine.name} added",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{ e ->
                Log.w(TAG,"Error getting getting data",e)
            }
    }
    fun toArrayListExercise(exercises: ArrayList<ArrayList<String>>): ArrayList<Exercise>{
        var arrayList:ArrayList<Exercise> = ArrayList()
        for (parametersExercise in exercises) {
            arrayList.add(Exercise(parametersExercise[0],parametersExercise[1],parametersExercise[2],parametersExercise[3],parametersExercise[4]))
        }
        return arrayList
    }
}
