package com.stayfit.ui.workouts.exercises

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.webkit.URLUtil
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.stayfit.R
import com.stayfit.ui.myroutines.Routine
import kotlinx.android.synthetic.main.activity_exercise.*


class ExerciseActivity: AppCompatActivity() {
    var progressBar: ProgressBar? = null
    var myCountDownTimer: MyCountDownTimer? = null
    var text_time: TextView? = null
    var title:TextView? = null
    private var delay:Delay = Delay(10000, 1000)
    var url_video: String = ""
    private lateinit var mAuth: FirebaseAuth
    var finished: Boolean = false
    var params: ArrayList<String> = ArrayList()
    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()
    private val TAG = "ExerciseActivity"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        mAuth = FirebaseAuth.getInstance()
        title = findViewById<TextView>(R.id.txt_exercise)
        val btn_ok = findViewById<Button>(R.id.btn_ok_exercise)
        btn_ok.visibility = View.GONE
        val intent:Intent = intent
        params = intent.getStringArrayListExtra("exercise_name")!!
        managerParametersIntent(params)

    }
    private fun downProgressBar(durada: Long){
        myCountDownTimer = MyCountDownTimer(durada, 1000)
        progressBar!!.max=(durada / 1000).toInt()
        progressBar!!.progress=progressBar!!.max
        text_time!!.setText(progressBar!!.progress.toString()+'"'+"/"+progressBar!!.max.toString()+'"')
        myCountDownTimer!!.start()
    }

    inner class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            progressBar!!.progress =  (millisUntilFinished / 1000).toInt()
            text_time!!.setText(progressBar!!.progress.toString()+'"'+"/"+progressBar!!.max.toString()+'"')
        }
        override fun onFinish() {
            finish()
        }
    }
    inner class Delay(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        fun setFalseBoolean(){
            finished = false
        }
        override fun onTick(millisUntilFinished: Long) {
        }
        override fun onFinish() {
            finished = true
        }
    }
    fun openinYT(view: View) {
        if (!URLUtil.isValidUrl(url_video)){ Toast.makeText(this, "This exercise doesn't have an associated video.", Toast.LENGTH_SHORT).show()}
        else{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url_video))
            startActivity(intent)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun managerParametersIntent(parameterIntent: ArrayList<String>){
        val name_exercise: String = parameterIntent[0]
        title!!.text = name_exercise
        url_video = parameterIntent[1]

        val progBar = findViewById<ProgressBar>(R.id.progressBar_exercise)
        val txt_time = findViewById<TextView>(R.id.txt_time_exercise)

        var time: Int = parameterIntent[2].toInt()
        if (time==0){
            if (parameterIntent[5] != "null"){
                txt_time.text = "x${parameterIntent[5]}"
                txt_time.textSize = (25).toFloat()
                txt_time.setTextColor(Color.parseColor("#6c63ff"))
                val btn_ok = findViewById<Button>(R.id.btn_ok_exercise)
                btn_ok.visibility = View.VISIBLE
            }else{
                txt_time.setText(parameterIntent[4]) // set description
                txt_time.textSize = (14).toFloat()
            }
            //txt_time.setTextColor(Color.BLACK)
            progBar.visibility = View.GONE
        }else{
            progressBar = findViewById<ProgressBar>(R.id.progressBar_exercise)
            text_time = findViewById<TextView>(R.id.txt_time_exercise)
            progressBar!!.getProgressDrawable().setColorFilter(Color.parseColor("#6c63ff"), android.graphics.PorterDuff.Mode.SRC_IN);
            var t: Long = (time*1000).toLong() // convert seconds to ms
            downProgressBar(t) //10s
        }

        var jason: String = parameterIntent[3]
        //setJason(parameterIntent[3])
        val js = findViewById<com.airbnb.lottie.LottieAnimationView>(R.id.jason_exercise)
        if (!jason.equals("null")){
            jason_exercise.setAnimation(jason)
        } else{
            jason_exercise.setAnimation("character_running.json")
        }
        jason_exercise.playAnimation()
        jason_exercise.loop(true)
    }

    fun addToRoutine() {
        //var : ArrayList<Routine> = ArrayList()
        val builderSingle = AlertDialog.Builder(this)
        var arrayExerciseAdapter: ArrayAdapter<ArrayList<Exercise>> ?= null
        builderSingle.setIcon(R.drawable.ic_assignment_purple)
        builderSingle.setTitle("Choose a routine")
        var  routinesList = ArrayAdapter<Routine>(
            this,
            android.R.layout.select_dialog_singlechoice
        )
        val arrayAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.select_dialog_singlechoice
        )
        arrayExerciseAdapter = ArrayAdapter<ArrayList<Exercise>>(
            this,
            android.R.layout.select_dialog_singlechoice
        )
        val user = mAuth.currentUser?.uid.toString()
        db.collection("routines").document(user).collection("MyRoutines")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val routineObj = document.data as HashMap<*, *>
                    val routine = Routine()
                    var h: HashMap<String,ArrayList<ArrayList<String>>> = HashMap()
                    //arrayAdapter.add(routineObj["name"].toString())
                    with(routine) {
                        name    = routineObj["name"].toString()
                        arrayAdapter.add(routineObj["name"].toString())
                        description  = routineObj["description"].toString()
                        photo   = routineObj["photo"].toString()
                        hashMapExercises  = h
                        //exercisesRoutine!!.add(routineObj["hashMapExercises"] as ArrayList<Exercise>)
                        //Log.d(TAG, "Get exs[0] ${routineObj["hashMapExercises"]!!.javaClass}")
                        Log.d(TAG, "Get exs[0] ${routineObj["hashMapExercises"]}")
                        var a:ArrayList<HashMap<*,String>> = ArrayList()
                        if (routineObj["hashMapExercises"] != null){ a= routineObj["hashMapExercises"] as ArrayList<HashMap<*,String>>}
                        arrayExerciseAdapter!!.add(toArrayListExercise2(a))
                        //Log.d(TAG, "exercisesList: ${routineObj["hashMapExercises"] as ArrayList<Exercise>}")
                        Log.d(TAG, "routine: $routine")
                        time_be = routineObj["time_be"].toString()
                        routinesList.add(routine)
                    }
                    Log.d(TAG, "routineList: $routinesList")
                }
                Log.d(TAG, "END2")
                //myroutinesRecycler.adapter!!.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

        builderSingle.setNegativeButton(
            "cancel"
        ) { dialog, which -> dialog.dismiss() }

        builderSingle.setAdapter(
            arrayAdapter
        ) { dialog, which ->
            val strName = arrayAdapter.getItem(which)
            val builderInner = AlertDialog.Builder(this)
            builderInner.setMessage(strName)
            var r:Routine ?= null
            for (i in 0 until routinesList.getCount()) {
                Log.d(TAG, "load exercisesList: ${arrayExerciseAdapter.getItem(i)}")
                var h: HashMap<String,ArrayList<ArrayList<String>>> = HashMap()
                h["exercises"] = arrayListExerciseToArrayListStrings(arrayExerciseAdapter.getItem(i)!!)
                routinesList.getItem(i)?.hashMapExercises = h
            }
            for (i in 0 until routinesList.getCount()) {
                if (strName.equals(routinesList.getItem(i)?.name)){
                    r = routinesList.getItem(i)
                    break
                }
            }
            Log.d(TAG, "routineSelected: $r")
            var exes:ArrayList<ArrayList<String>> = ArrayList()
            if (r!!.hashMapExercises?.get("exercises")!=null) {
                exes = r.hashMapExercises?.get("exercises") as ArrayList<ArrayList<String>>
            }
            exes.add(params)
            r.hashMapExercises?.set("exercises", exes)
            Log.d(TAG, "params $params")
            val routine = hashMapOf(
                "name" to r.name,
                "description" to r.description,
                "photo" to r.photo,
                "hashMapExercises" to toArrayListExercise(exes) ,
                "time_be" to r.time_be
            )
            db.collection("routines").get()
                .addOnSuccessListener { result ->
                    var userFound = false
                    for(user in result){
                        if(user.id.equals(user)){
                            userFound=true
                        }
                    }
                    if(userFound){
                        db.collection("routines").document(user).collection("MyRoutines").document(r.name).set(routine)
                    }else{
                        //Adding User credentials
                        val dataUser = HashMap<String, String>()
                        dataUser["user_Auth"] = mAuth.currentUser?.email.toString()
                        db.collection("routines").document(user).set(dataUser)
                        //Adding User Routine
                        db.collection("routines").document(user).collection("MyRoutines").document(r.name).set(routine)
                        Log.d(TAG,"Routine added.")
                    }
                }
                .addOnFailureListener{ e ->
                    Log.w(TAG,"Error getting getting data",e)
                }

            builderInner.setTitle("Your exercies has been added to")
            builderInner.setPositiveButton(
                "Ok"
            ) { dialog, which -> dialog.dismiss() }
            builderInner.show()
        }
        builderSingle.show()
    }

    fun getRoutinesNamesList(routinesList: ArrayList<Routine>): ArrayList<String>{
        Log.d(TAG, "List: $routinesList")
        var namesRoutines: ArrayList<String> = ArrayList()
        for (r in routinesList){ namesRoutines.add(r.name)}
        return namesRoutines
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
                        exercise["description"],
                        exercise["reps"]
                    )
                )
            }
        }

        return arrayList
    }
    fun toArrayListExercise(exercises: ArrayList<ArrayList<String>>): ArrayList<Exercise>{
        var arrayList:ArrayList<Exercise> = ArrayList()
        for (parametersExercise in exercises) {
            arrayList.add(Exercise(parametersExercise[0],parametersExercise[1],parametersExercise[2],parametersExercise[3],parametersExercise[4],parametersExercise[5]))
        }
        return arrayList
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
    fun setTime (view: View){
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Do you want to do the exercise by time or by reps?")
        val categories = arrayOf("TIME","REPS")
        builder.setItems(categories) { dialog, which ->
            when (which) {
                0 -> { val timeDialog = AlertDialog.Builder(this)
                    val mView:View = getLayoutInflater().inflate(R.layout.exercise_set_time,null)
                    val editText: EditText = mView.findViewById(R.id.editTextTimeExercise)
                    timeDialog.setPositiveButton("Apply") { dialog, which ->
                        params[2] = editText.text.toString()
                        addToRoutine()
                    }
                    timeDialog.setNegativeButton("Cancel") { dialog, which ->
                        params[2] = "0"
                    }
                    timeDialog.setView(mView)
                    timeDialog.show() }
                1 -> {val timeDialog = AlertDialog.Builder(this)
                    val mView:View = getLayoutInflater().inflate(R.layout.exercise_set_reps,null)
                    val editText: EditText = mView.findViewById(R.id.editTextRepsExercise)
                    timeDialog.setPositiveButton("Apply") { dialog, which ->
                        params[5] = editText.text.toString()
                        addToRoutine()
                    }
                    timeDialog.setNegativeButton("Cancel") { dialog, which ->
                        params[5] = "null"
                    }
                    timeDialog.setView(mView)
                    timeDialog.show()
                    }
            }
        }
        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()

    }

    fun finishOK(view: View) {
        finish()
    }
    /*
    fun setJason(jason: String){
        when(jason) {
            "" -> println("Invalid number")
            1, 2 -> println("Number too low")
            3 -> println("Number correct")
            4 -> println("Number too high, but acceptable")
            else -> {val js = findViewById<com.airbnb.lottie.LottieAnimationView>(R.id.jason_exercise)
                js.setAnimation(R)}
        }
    } */
}
