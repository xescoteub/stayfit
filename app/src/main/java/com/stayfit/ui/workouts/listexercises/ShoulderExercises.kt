package com.stayfit.ui.workouts.listexercises

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.stayfit.R
import com.stayfit.ui.workouts.exercises.Exercise
import com.stayfit.ui.workouts.exercises.ExerciseActivity

class ShoulderExercises : AppCompatActivity() {
    //create object of listview
    var listView: ListView ?= null
    //create ArrayList of String
    var arrayList: ArrayList<String> = ArrayList()
    //create ArrayList of String
    var exerciseList: ArrayList<Exercise> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shoulder_exercises)
        listView = findViewById<View>(R.id.shoulder_list) as ListView
        controlListView()
    }
    fun controlListView(){
        //Add elements to arraylist
        var barbellOverheadShoulderPress: Exercise = Exercise("Barbell Overhead Shoulder Press","https://www.youtube.com/watch?v=5yWaNOvgFCM", "0", "null", "", "null")
        addExercise(barbellOverheadShoulderPress)

        addExercise( Exercise("Seated Dumbbell Shoulder Press","https://www.youtube.com/watch?v=0JfYxMRsUCQ", "0", "null","Re-rack your barbell and grab a pair of dumbbells, because they're all you'll need for the rest of the workout. Next grab a bench; sitting down helps isolate the shoulder motion. \"These are really good for not only handling a lot of weight, but also having the freedom of using dumbbells,\" White tells MH. \"You can bring them nice and low to get a really full range of motion.\" Take a load off your feet and put a load on your delts.", "null"))
        //arrayList.add("Seated Dumbbell Shoulder Press")
        addExercise( Exercise("Front Raise","https://www.youtube.com/watch?v=-t7fuZ0KhDA", "0", "null","", "null"))
        //arrayList.add("Front Raise")
        addExercise( Exercise("Reverse Pec Deck Fly","https://www.youtube.com/watch?time_continue=0&v=R63UajjKfnM&feature=emb_title", "0", "null","", "null"))
        //arrayList.add("Reverse Pec Deck Fly")
        addExercise( Exercise("Bent-Over Dumbbell Lateral Raise","https://www.youtube.com/watch?v=ttvfGg9d76c&feature=emb_title", "0", "null","", "null"))
        //arrayList.add("Bent-Over Dumbbell Lateral Raise")
        addExercise( Exercise("Dumbbell Lateral Raise","https://www.youtube.com/watch?v=-WEZj9ePTYI", "0", "null","", "null"))
        //arrayList.add("Dumbbell Lateral Raise")
        addExercise( Exercise("Push Press","https://www.youtube.com/watch?v=iaBVSJm78ko", "0", "null","", "null"))
        //arrayList.add("Push Press")
        addExercise( Exercise("Reverse Cable Crossover","https://www.youtube.com/watch?v=JENKmsEZQO8", "0", "null","", "null"))
        //arrayList.add("Reverse Cable Crossover")
        addExercise( Exercise("One-Arm Cable Lateral Raise","https://www.youtube.com/watch?v=_w6ztKA4tO8", "0", "null","", "null"))
        //arrayList.add("One-Arm Cable Lateral Raise")
        addExercise( Exercise("Standing Barbell Shrugs","https://www.youtube.com/watch?v=NAqCVe2mwzM", "0", "null","", "null"))
        //arrayList.add("Standing Barbell Shrugs")
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
    private fun addExercise(exercise: Exercise){
        exerciseList.add(exercise)
        arrayList.add(exercise.getExerciseName())
    }
}
