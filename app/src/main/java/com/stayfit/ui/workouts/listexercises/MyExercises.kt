package com.stayfit.ui.workouts.listexercises

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stayfit.R
import com.stayfit.ui.workouts.exercises.Exercise
import com.stayfit.ui.workouts.exercises.ExerciseActivity
import java.lang.Exception
import java.lang.reflect.Type


class MyExercises : AppCompatActivity() {
    //create object of listview
    var listView: ListView?= null
    //create ArrayList of String
    var arrayList: ArrayList<String> = ArrayList()
    //create ArrayList of String
    var exerciseList: ArrayList<Exercise> = ArrayList()

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()
    private val TAG = "MyExercises"
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_exercises)
        mAuth = FirebaseAuth.getInstance()
        listView = findViewById<ListView>(R.id.my_exercises_list)
        controlListView()
    }
    fun controlListView(){
        //Add elements to arraylist
        try {
            loadData()
        }catch (e: Exception){

        }
        //Create Adapter
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any>(this, android.R.layout.simple_list_item_1, arrayList as List<Any>?)
        //assign adapter to listview
        listView?.adapter = arrayAdapter
        //add listener to listview
        listView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l -> startConcreteExercise(i) }

    }
    fun startConcreteExercise(i: Int){
        if (i<exerciseList.size){startExercise(exerciseList[i])}
        else{ Toast.makeText(this, "Exercise ${arrayList[i]} will be available coming soon!", Toast.LENGTH_SHORT).show()}
    }
    private fun startExercise(exercise: Exercise) {
        val intent = Intent(this, ExerciseActivity::class.java)
        intent.putExtra("exercise_name",exercise.getParametersList());
        startActivity(intent)
    }
    fun addExercise(exercise: Exercise){
        exerciseList.add(exercise)
        arrayList.add(exercise.getExerciseName())
        saveData(exercise)
    }

    fun newExercise(view: View) {
        val intent = Intent(this, FormExercise::class.java)
        startActivityForResult(intent, 2);// Activity is started with requestCode 2
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // https://developer.android.com/training/basics/intents/result  https://stackoverflow.com/questions/19666572/how-to-call-a-method-in-another-activity-from-activity
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            if (resultCode == 2){
                var arrayList: ArrayList<String> = data!!.getStringArrayListExtra("LIST")
                addExercise(Exercise( arrayList[0], arrayList[2],"0", "null", arrayList[1]))
                controlListView()
            }
        }
    }
    private fun saveData(e: Exercise){
        val exercise = hashMapOf(
            "nom_exercise" to e.nom_exercise,
            "url_video" to e.url_video,
            "time_count" to e.time_count,
            "jason" to e.jason,
            "description" to e.description
        )
        val currentUserID = mAuth.currentUser?.uid.toString()
        db.collection("myexercises").get()
            .addOnSuccessListener { result ->
                var userFound = false
                for(user in result){
                    if(user.id.equals(currentUserID)){
                        userFound=true
                    }
                }
                if(userFound){
                    db.collection("myexercises").document(currentUserID).collection("exercicis").document(e.nom_exercise!!).set(exercise)
                }else{
                    //Adding User credentials
                    val dataUser = HashMap<String, String>()
                    dataUser["user_Auth"] = mAuth.currentUser?.email.toString()
                    db.collection("myexercises").document(currentUserID).set(dataUser)
                    //Adding User Routine
                    db.collection("myexercises").document(currentUserID).collection("exercicis").document(e.nom_exercise!!).set(exercise)
                    Log.d(TAG,"Exercise added.")
                    Toast.makeText(this,"Exercise ${e.nom_exercise} added",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{ e ->
                Log.w(TAG,"Error getting getting data",e)
            }

    }
    private fun loadData(){
        val user = mAuth.currentUser?.uid.toString()
        db.collection("myexercises").document(user).collection("exercicis")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    val exerciseObj = document.data as HashMap<*, *>
                    val exercise = Exercise()

                    with(exercise) {
                        nom_exercise    = exerciseObj["nom_exercise"].toString()
                        url_video = exerciseObj["url_video"].toString()
                        time_count  = exerciseObj["time_count"].toString()
                        jason = exerciseObj["jason"].toString()
                        description  = exerciseObj["description"].toString()
                    }
                    Log.d(TAG, "exercise: $exercise")
                    exerciseList.add(exercise)
                    Log.d(TAG, "exerciseList: $exerciseList")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

    }
    fun deleteItem(view: View?) {
        listView?.onItemClickListener = OnItemClickListener { a, v, position, id ->
            val adb: AlertDialog.Builder = AlertDialog.Builder(this)
            adb.setTitle("Delete?")
            adb.setMessage("Are you sure you want to delete ${arrayList.get(position)}?")
            adb.setNegativeButton("Cancel", null)
            adb.setPositiveButton(
                "Ok"
            ) { dialog, which ->
                arrayList.removeAt(position)
                exerciseList.removeAt(position)
                //saveData()
                controlListView()
            }
            adb.show()
        }
    }
    /*
    fun getParametersList(exercise: Exercise): ArrayList<String>{
        var list: ArrayList<String> = ArrayList()
        list.add(exercise.nom_exercise!!)
        list.add(exercise.url_video!!)
        list.add(exercise.time_count!!)
        list.add(exercise.jason!!)
        list.add(exercise.description!!)
        return list
    }*/
}

