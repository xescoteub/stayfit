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

class BackExercises : AppCompatActivity() {
    //create object of listview
    var listView: ListView ?= null
    //create ArrayList of String
    var arrayList: ArrayList<String> = ArrayList()
    //create ArrayList of String
    var exerciseList: ArrayList<Exercise> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_exercises)
        listView = findViewById<View>(R.id.back_list) as ListView
        controlListView()
    }
    fun controlListView(){
        //Add elements to arraylist
        var deadlift: Exercise = Exercise("Deadlift", "https://www.youtube.com/watch?v=op9kVnSso6Q", "0", "null", "Why: The true king of compound movements, the barbell deadlift is a full-body move — building stronger legs, back, shoulders and arms. Its place in your next back workout is well deserved — as you work through the full range of motion, your upper-back muscles (rhomboids, traps, rear delts and lats) are firing away helping to keep your torso straight, while preventing your back from rounding and causing injury.\n" +
                "\n" +
                "How: Squat down and grasp a barbell with your hands roughly shoulder-width apart. Keep your chest up, pull your shoulders back and look straight ahead as you lift the bar. Focus on taking the weight back onto your heels and keep the bar as close as possible to your body at all times. Lift to thigh level, pause, then return under control to the start position.")
        addExercise(deadlift)

        addExercise( Exercise("Pull-Up","https://www.youtube.com/watch?v=aAggnpPyR6E", "0", "null","Why: If you want a V-shape physique — you do, that’s why you’re here — then there’s no avoiding pull-ups. Targeting your lats directly, you’ll gain a wider frame and will appear slimmer. Plus, you’ll get major gym kudos once your chin goes above that bar.\n" +
                "\n" +
                "How: Grab the handles of the pull-up station with your palms facing away from you and your arms fully extended. Your hands should be around shoulder-width apart. Squeeze your shoulder blades together, exhale and drive your elbows towards your hips to bring your chin above the bar. Lower under control back to the start position."))
        //arrayList.add("Pull-Up")
        addExercise( Exercise("Bent Over Row","https://www.youtube.com/watch?v=vT2GjY_Umpw", "0", "null","Why: As you’re working with a barbell, you should be able to shift more weight during a barbell bent-over row. Helping your recruit more muscle — and, obviously, elicit further muscle growth — you’ll work your middle and lower traps, rhomboid major, rhomboid minor, upper traps, rear deltoids, and rotator cuff muscles. Keep your shoulder blades back to avoid slouching, which puts undue stress on your lower back.\n" +
                "\n" +
                "How: Grab a barbell with an overhand grip, hands slightly wider than shoulder width apart. With your legs slightly bent, keep your back perfectly straight and bend your upper body forward until it’s almost perpendicular to the floor. From here row the weight upwards into the lower part of your chest. Pause. And return under control to the start position."))
        //arrayList.add("Bent Over Row")
        addExercise( Exercise("T-Bar/Chest Supported Row","https://www.youtube.com/watch?v=sSHJlTnXekM", "0", "null","Why: An ideal move for those struggling to keep the chest strong and spine straight during other back-building bent-over moves, the chest-supported dumbbell row isolates your back muscles — helping move the dumbbells considerably more efficiently and safely.\n" +
                "\n" +
                "How: Lie face down on the bench with your feet other side to keep you stable. Hang the dumbbells beneath you using a neutral grip. Keep your head up and bring your shoulder blades together as you row the weights towards your chest. Lower to the starting position under control."))
        //arrayList.add("T-Bar/Chest Supported Row")
        addExercise( Exercise("Standing T-Bar Row","https://www.youtube.com/watch?v=KDEl3AmZbVE", "0", "null","Muscles worked: Additionally, this exercise works all the major back muscles – teres major, trapezius and erector spinae. The movement also works the shoulders and the pulling muscles in your arms — biceps, brachialis, and brachioradialis. The stabilizers include the abdominals, hamstrings, and glutes. When executing the T-Bar Row, it is important to establish correct form in order to avoid risk of back injury.\n" +
                "\n" +
                "How : "))
        //arrayList.add("Standing T-Bar Row")
        addExercise( Exercise("Single-Arm Dumbbell Row","https://www.youtube.com/watch?v=T0QAh5oaJV8", "0", "null","Why: Another great move for your lats, the dumbbell single arm row works both sides of your body and helps you focus (and fix) weaker spots by smashing through strength imbalances on either side. A handy tip: don't let your shoulder drop at the bottom of the movement. Lock your torso to ensure your back lifts the weight, not your arm.\n" +
                "\n" +
                "How: Head to a flat bench and place your right hand against it under your shoulder, keeping your arm straight. Rest your right knee on the bench and step your other leg out to the side. With your free hand grab a dumbbell off the floor and row it up to your side until your upper arm is parallel with the floor. Lower slowly back to the floor and repeat."))
        //arrayList.add("Single-Arm Dumbbell Row")
        addExercise( Exercise("Inverted Row","https://www.youtube.com/watch?v=XZV9IwluPjw", "0", "null","Why: Suitable for those struggling with pull-ups and chin-ups, the inverted row is surprisingly difficult. Smoking your back and your arms, you can progress or regress the move by re-arranging where your feet.\n" +
                "\n" +
                "How: Set up a bar in a rack at waist height. Grab it with a wider than shoulder-width overhand grip and hang underneath. Position yourself with heels out in front of you and arms fully extended. Your body should be straight from shoulders to ankles. Flex at the elbows to pull your chest up to the bar. Lower yourself back to the start position under control."))
        //arrayList.add("Inverted Row")
        addExercise( Exercise("Lat Pulldown","https://www.youtube.com/watch?v=0oeIB6wi3es", "0", "null","Why: Just like pull-ups, lat pull-downs — a firm bodybuilding favourite — will build your lats, while working at a slow tempo will maximise your muscle gain. Keep form strict and reap the rewards. A tip: always bring the bar in front of your head. The behind-the-neck version can damage your rotator cuff.\n" +
                "\n" +
                "How: Kneel in front of the cable machine and face away. Grab the bar with your palms facing away from you, shoulder-width apart. Lean back slightly and push your chest out. Pull the bar down to your chest, then return slowly to the start position. Your torso should remain still throughout."))
        //arrayList.add("Lat Pulldown")
        addExercise( Exercise("Close-Grip Pull-Down","https://www.youtube.com/watch?v=neP32qCyPbQ", "0", "null",""))
        //arrayList.add("Close-Grip Pull-Down")
        addExercise( Exercise("Loaded Carries","https://www.youtube.com/watch?v=SsrAVtw-UaI", "0", "null",""))
        //arrayList.add("Loaded Carries")
        addExercise( Exercise("Seated Cable Row","https://www.youtube.com/watch?v=GZbfZ033f74", "0", "null",""))
        //arrayList.add("Seated Cable Row")
        addExercise( Exercise("Cable Pull Over","https://www.youtube.com/watch?v=Act5-TOl7M8", "0", "null",""))
       // arrayList.add("Cable Pull Over")
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
