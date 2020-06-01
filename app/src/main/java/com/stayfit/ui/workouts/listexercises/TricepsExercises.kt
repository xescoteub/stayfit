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

class TricepsExercises : AppCompatActivity() {
    //create object of listview
    var listView: ListView ?= null
    //create ArrayList of String
    var arrayList: ArrayList<String> = ArrayList()
    //create ArrayList of String
    var exerciseList: ArrayList<Exercise> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_triceps_exercises)
        listView = findViewById<View>(R.id.triceps_list) as ListView
        controlListView()
    }
    fun controlListView(){
        //Add elements to arraylist
        var closegripBenchPress: Exercise = Exercise("Close-grip Bench Press", "https://www.youtube.com/watch?v=wxVRe9pmJdk", "0", "null", "The bench press is a great tricep exercise to work your chest and core. Placing your hands closer together makes it so your triceps have to work harder, which can lead to new growth and more strength.\n" +
                "\n" +
                "How to do it:\n" +
                "\n" +
                "- Grasp a barbell with an overhand grip that’s shoulder-width apart, and hold it above your sternum with arms completely straight.\n" +
                "\n" +
                "- Lower the bar straight down, pause, and then press the bar back up to the starting position.", "null")
        addExercise(closegripBenchPress)

        addExercise( Exercise("Rope Tricep Pushdown","https://www.youtube.com/watch?v=8CbJK7mmisE", "0", "null","This move zones in on your triceps – but only if you do it right. If you use too much weight, you’ll involve your back and shoulder muscles, defeating the purpose. If you can’t keep your shoulders down, lighten the load.\n" +
                "\n" +
                "How to do it:\n" +
                "\n" +
                "- Attach a rope handle to the high pulley of a cable station. Bend your arms and grab the bar with an overhand grip, your hands shoulder-width apart. Tuck your upper arms next to your sides.\n" +
                "\n" +
                "- Without moving your upper arms, push the bar down until your elbows are locked. Slowly return to the starting position.", "null"))
        //arrayList.add("Rope Tricep Pushdown")
        addExercise( Exercise("Tricep Dips","https://www.youtube.com/watch?v=6kALZikXxLc", "0", "null","\n" +
                "3. Tricep Dips (Advanced)\n" +
                "Because you’re lifting your entire bodyweight, your triceps have to work against a much heavier load than they would in a triceps-isolating exercise.\n" +
                "\n" +
                "RELATED STORY\n" +
                "\n" +
                "Grow Huge Arms with This 6-move Superset Workout\n" +
                "How to do it:\n" +
                "\n" +
                "- Hoist yourself up on parallel bars with your torso perpendicular to the floor; you'll maintain this posture throughout the exercise. (Leaning forward will shift emphasis to your chest and shoulders.)\n" +
                "\n" +
                "- Bend your knees and cross your ankles. Slowly lower your body until your shoulder joints are below your elbows. (Most guys stop short of this position.)\n" +
                "\n" +
                "- Push back up until your elbows are nearly straight but not locked. If you have shoulder issues, skip this move.", "null"))
        //arrayList.add("Tricep Dips")
        addExercise( Exercise("Overhead Triceps Extension","https://www.youtube.com/watch?v=_gsUck-7M74", "0", "null","When you work your triceps, you might forget there are three parts to the muscle: the lateral head, the medial head, and the long head. The last part might not always get the attention it deserves – unless you're regularly doing exercises like this one, with your arms over your head to isolate the long head.\n" +
                "\n" +
                "How to do it:\n" +
                "\n" +
                "- Sit on a bench and grab one dumbbell. Form a diamond shape with both hands to grip the top end of the weight. Raise the dumbbell over your head, keeping your elbows up and your core tight.\n" +
                "\n" +
                "- Lower the dumbbell down the top of your back by bending at the elbow, maintaining your strong chest and keeping your shoulders still.\n" +
                "\n" +
                "- Raise the weight by fully extending your arms, pausing for a count to squeeze at the top of the movement.", "null"))
        //arrayList.add("Overhead Triceps Extension")
        addExercise( Exercise("Skullcrushers","https://www.youtube.com/watch?v=ir5PsbniVSc", "0", "null","Whilst there are many variations of this move, they all have one thing in common: elbow extension. As the upper arms are locked in position, the long and lateral tricep heads are called into play. Increasing the angle of an incline bench will work your triceps long head, while doing the movement on a decline bench places more emphasis on the lateral triceps head.\n" +
                "\n" +
                "How to do it:\n" +
                "\n" +
                "- Grip the EZ bar on the inner grips using an overhand grip and extend your arms straight up.\n" +
                "\n" +
                "- Keeping your elbows fixed and tucked in, slowly lower the bar until it is about an inch from your forehead. Always keep your upper arms perpendicular to the floor.\n" +
                "\n" +
                "- Slowly extend your arms back to the starting position without locking your elbows.", "null"))
        //arrayList.add("Skullcrushers")
        addExercise( Exercise("The Diamond Press-up","https://www.youtube.com/watch?v=pD3mD6WgykM", "0", "null","The standard press-up is great for your chest and arms, but moving your hands closer together puts the emphasis squarely on your triceps. You're still going to get some work for your pecs with this variation, but your tris should really feel the burn by the time you're through.\n" +
                "\n" +
                "How to do it:\n" +
                "\n" +
                "- Lower yourself down into a standard plank or press-up position. Bring your hands close to each other at chest level, with your thumbs touching one another and your forefingers touching. Your spine should be straight, and your core and glutes should be squeezed tight.\n" +
                "\n" +
                "- Lower yourself down to the floor. Pause, maintaining the squeeze in your core and glutes, then push back up to the original position by straightening your arms.", "null"))
        //arrayList.add("The Diamond Press-up")
        addExercise( Exercise("Dumbbell Floor Press","https://www.youtube.com/watch?v=lNdi7VEf2Ew", "0", "null","This variation of a classic bench press favours the lockout portion of the lift, which recruits your triceps to an extreme degree. And since the load is distributed differently with a dumbbell than a barbell, your stabilising muscles have to work harder to keep the weight positioned correctly.\n" +
                "\n" +
                "How to do it:\n" +
                "\n" +
                "- Grab a dumbbell with each hand and lie with your back on the ground.\n" +
                "\n" +
                "- Hold the dumbbells overhead and bend your arm to lower the kettlebells.\n" +
                "\n" +
                "- Touch your elbows to the ground, pause, then press them back up.", "null"))
        //arrayList.add("Dumbbell Floor Press")
        addExercise( Exercise("The Classic Press-up","https://www.youtube.com/watch?v=IODxDxX7oi4", "0", "null","The old ones are the best ones. The traditional press-up works your chest, core and your triceps. The beauty with this move is that it can be performed anywhere. You can make it harder by wearing a weighted vest.\n" +
                "\n" +
                "How to do it:\n" +
                "\n" +
                "- Set up with your weight supported on your toes and hands beneath your shoulders, body straight. Take care to keep you core locked so a straight line forms between your head, glutes and heels.\n" +
                "\n" +
                "- Lower your body until your chest is an inch from the ground then explosively drive up by fully extending your arms.", "null"))
        //arrayList.add("The Classic Press-up")
        addExercise( Exercise("One Arm Kettlebell Floor Press","https://www.youtube.com/watch?v=B340QckIfJM", "0", "null","Using one arm at a time isolates the chest and triceps, ensuring the muscles are worked hard.\n" +
                "\n" +
                "How to do it:\n" +
                "\n" +
                "- Lie on the floor and hold a kettlebell in one hand, with your upper arm being supported by the floor.\n" +
                "\n" +
                "- Extend your arm and press the kettlebell straight up toward the ceiling. That's one rep. Lower the kettlebell and repeat.", "null"))
        //arrayList.add("One Arm Kettlebell Floor Press")
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
