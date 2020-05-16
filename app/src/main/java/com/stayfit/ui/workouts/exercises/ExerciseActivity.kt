package com.stayfit.ui.workouts.exercises

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.webkit.URLUtil
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.stayfit.R
import com.stayfit.ui.myroutines.MyRoutinesFragment
import com.stayfit.ui.myroutines.MyRoutinesViewModel
import com.stayfit.ui.myroutines.Routine
import kotlinx.android.synthetic.main.fragment_my_routines.*


class ExerciseActivity: AppCompatActivity() {
    var progressBar: ProgressBar? = null
    var myCountDownTimer: MyCountDownTimer? = null
    var text_time: TextView? = null
    var title:TextView? = null
    private var delay:Delay = Delay(10000, 1000)
    var url_video: String = ""
    private lateinit var mAuth: FirebaseAuth
    var finished: Boolean = false
    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()
    private val TAG = "ExerciseActivity"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        mAuth = FirebaseAuth.getInstance()
        title = findViewById<TextView>(R.id.txt_exercise)
        val intent:Intent = intent
        managerParametersIntent(intent.getStringArrayListExtra("exercise_name"))

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
            txt_time.setText(parameterIntent[4]) // set description
            txt_time.textSize = (14).toFloat()
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
        if (!jason.equals("null")){
            val js = findViewById<com.airbnb.lottie.LottieAnimationView>(R.id.jason_exercise)
            js.setAnimation(jason)
        }
    }

    fun addToRoutine(view: View) {
        var routinesList: ArrayList<Routine> = ArrayList()
        var exercisesRoutine: ArrayList<ArrayList<Exercise>> = ArrayList()
        val builderSingle = AlertDialog.Builder(this)
        builderSingle.setIcon(R.drawable.ic_assignment_purple)
        builderSingle.setTitle("Choose a routine")

        val arrayAdapter = ArrayAdapter<String>(
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

                    with(routine) {
                        name    = routineObj["name"].toString()
                        description  = routineObj["description"].toString()
                        photo   = routineObj["photo"].toString()
                        hashMapExercises  = h
                        exercisesRoutine!!.add(routineObj["hashMapExercises"] as ArrayList<Exercise>)
                        Log.d(TAG, "exercisesList: ${routineObj["hashMapExercises"] as ArrayList<Exercise>}")
                        Log.d(TAG, "routine: $routine")
                        routinesList.add(routine)
                        arrayAdapter.add(routineObj["name"].toString())
                    }
                    Log.d(TAG, "routineList: $routinesList")
                }
                //Log.d(TAG, "END2")
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
            builderInner.setTitle("Your Selected Item is")
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
}
