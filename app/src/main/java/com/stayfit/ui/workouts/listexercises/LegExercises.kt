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

class LegExercises : AppCompatActivity() {
    //create object of listview
    var listView: ListView ?= null
    //create ArrayList of String
    var arrayList: ArrayList<String> = ArrayList()
    //create ArrayList of String
    var exerciseList: ArrayList<Exercise> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leg_exercises)
        listView = findViewById<View>(R.id.leg_list) as ListView
        controlListView()
    }
    fun controlListView(){
        //Add elements to arraylist
        var standingForwardBend: Exercise = Exercise("Standing Forward Bend","https://www.youtube.com/watch?v=2jvX4_vc_u0", "0", "null", "", "null")
        addExercise(standingForwardBend)

        addExercise( Exercise("Kneeling Hip Flexor Stretch","https://www.youtube.com/watch?v=kyWMLob5SGk", "0", "null","", "null"))
        //arrayList.add("Kneeling Hip Flexor Stretch")
        addExercise( Exercise("Leg Press","https://www.youtube.com/watch?v=Aq5uxXrXq7c", "0", "null","How to:  Sit on the leg press machine. Place your feet on the footplate about hip-width apart while ensuring that your heels are flat. Your bottom should be flat against the seat rather than raised. Your legs should form an angle of about 90 degrees at the knees. Extend your legs and keep your head and back flat against the seat pad. Finally return to the initial position. That’s one rep. We recommend to do 15 reps ", "null"))
        //arrayList.add("Leg Press")
        addExercise( Exercise("Barbell Squats","https://www.youtube.com/watch?v=1oed-UmAxFs", "0", "null","How to: Stand with your feet more than shoulder-width apart hold a barbell across your upper back with an overhand grip – avoid resting it on your neck -. Take the weight of the bar and slowly squat down until your hips are aligned with your knees, with legs at 90 degrees. Drive your heels into the floor to push yourself explosively back up. Keep form until you’re stood up straight. That’s one rep. We recommend to do 12 reps", "null"))
        //arrayList.add("Barbell Squats")
        addExercise( Exercise("Jump Squat","https://www.youtube.com/watch?v=J6Y520KkwOA", "0", "null","How to: Stand tall with your feet hip-width apart. Hinge at the hips to push your butt back and lower down until your thighs are parallel to the floor. Then press your feet down to explode off the floor and jump as high as you can. That’s one rep. We recommend to do 20 reps", "null"))
        //arrayList.add("Jump Squat")
        addExercise( Exercise("Hack Squat","https://www.youtube.com/watch?v=0tn5K9NlCfo", "0", "null","How to: Step into the machine, placing your feet shoulder-width apart and your shoulders and back against the pads. Release the safety handles, inhale, and lower down, bending your knees until they reach a 90-degree angle. Pause here, then push up through the back of your feet to extend your legs back to the starting position. That’s one rep. We recommend to do 12 reps", "null"))
        //arrayList.add("Hack Squat")
        addExercise( Exercise("Romanian Deadlift","https://www.youtube.com/watch?v=2SHsk9AzdjA", "0", "null","How: Stand behind a grounded barbell. Bend your knees slightly to grab it, keeping your shins, back and hips straight. Without bending your back, push your hips forwards to lift the bar. From upright, push your hips back to lower the bar, bending your knees only slightly. Why: “It’s a great variation on the traditional deadlift that focuses on your glutes and superior end of your hamstrings,” says Leonard. Practising hip flexion exercises – like the Romanian deadlift – can seriously improve your sprint speed and agility. After eight weeks of hip flexor-specific training, participants in a University of Florida study saw their sprint and shuttle run PBs improve by around four per cent and nine per cent respectively.", "null"))
        //arrayList.add("Romanian Deadlift")
        addExercise( Exercise("Standing Calf Raises","https://www.youtube.com/watch?v=-M4-G8p8fmc", "0", "null","How to: Raise your heels a few inches above the edge of the step so that you’re on your tiptoes. Hold the position for a moment, and then lower your heels below the platform, feeling a stretch in your calf muscles. That’s one rep. We recommend to do 20 reps", "null"))
        //arrayList.add("Standing Calf Raises")
        addExercise( Exercise("Leg Extensions","https://www.youtube.com/watch?v=YyvSfVjQeL0", "0", "null","How to: Set up the leg extension machine so the pad is at the top of your lower legs at the ankles. Place your hands on the hand bars. Lift the weight until your legs are almost straight. Do not lock your knees. Keep your back against the backrest and do not arch your back. Lower the weight back to starting position.That’s one rep. We recommend to do 12 reps", "null"))
        //arrayList.add("Leg Extensions")
        addExercise( Exercise("Lying Leg Curls","https://www.youtube.com/watch?v=LxI-ec8cQYo", "0", "null","How to: Adjust the roller pad so that it rests comfortably a few inches under your calves, just above the heels. Stretch your legs out fully. Lift your feet smoothly, keeping your hips firmly on the bench. Inhale as you flex your knees and pull your ankles as close to your buttocks as you can. Hold this position for a beat and return to the initial position. That’s one rep. We recommend to do 12 reps", "null"))
        //arrayList.add("Lying Leg Curls")
        addExercise( Exercise("Dumbbell Step Ups","https://www.youtube.com/watch?v=S24Do-rZncI", "0", "null","How to: Stand in front of a step or box of the selected height, hold dumbbells in your hands. Step up with the right foot, pressing through the heel to straighten your right leg. Bring the left foot to meet your right foot on top of the step. Bend your right knee and step down with the left foot. Bring the right foot down to meet the left foot on the ground.That’s one rep. We recommend to do 15 reps", "null"))
        //arrayList.add("Dumbbell Step Ups")
        addExercise( Exercise("Reverse Lunge","https://www.youtube.com/watch?v=_ggxoUsCx7A", "0", "null","How to: Stand with feet shoulder-width apart, hands at side or on your hips. With your right foot, step back about one and a half times your normal stride length, landing with the ball of that foot on the ground and your heel up. Lower the back leg straight down until it grazes the ground, creating a 90-degree angle in the front leg. Bring your right foot back in line with your left. That’s one rep. We recommend to do 8 reps on each leg", "null"))
        //arrayList.add("Reverse Lunge")
        addExercise( Exercise("Walking Lunge","https://www.youtube.com/watch?v=L8fvypPrzzs", "0", "null","How to: Stand up straight with your feet shoulder-width apart. Your hands can stay by the side of your body or on your hips. Step forward with your right leg, putting the weight into your heel. Bend the right knee, lowering down so that it’s parallel to the floor in a lunge position. Pause for a beat. Without moving the right leg, move your left foot forward. That’s one rep. We recommend to do 15 reps", "null"))
        //arrayList.add("Walking Lunge")
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
