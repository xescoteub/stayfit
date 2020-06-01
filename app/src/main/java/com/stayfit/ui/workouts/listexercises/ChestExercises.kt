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

class ChestExercises : AppCompatActivity() {
    //create object of listview
    var listView: ListView ?= null
    //create ArrayList of String
    var arrayList: ArrayList<String> = ArrayList()
    //create ArrayList of String
    var exerciseList: ArrayList<Exercise> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chest_exercises)
        listView = findViewById<View>(R.id.chest_list) as ListView
        controlListView()
    }
    fun controlListView(){
        //Add elements to arraylist
        var dumbbellSqueezePress: Exercise = Exercise("Dumbbell Squeeze Press","https://www.youtube.com/watch?v=Aq_wwRslKas", "0", "null", "Why: Squeezing the dumbbells together during a chest press moves the emphasis of the movement onto your pecs. This simple tweak engages them throughout the entire range of motion — a key factor to maximise muscle gain.\n" +
                "\n" +
                "How: Lie on a flat bench and hold a dumbbell in each hand. Maintain a neutral grip and begin with your arms straight, directly above you. Bend your arms and lower them to the side of your body so the dumbbells lie just above your chest. Pause and then lift your arms to repeat.", "null")
        addExercise(dumbbellSqueezePress)

        addExercise( Exercise("Incline barbell bench press","https://www.youtube.com/watch?v=SrqOu55lrYU", "0", "null","Why: Pressing on an incline set-up works the clavicular head, which is why the incline barbell bench press makes your pecs pop.\n" +
                "\n" +
                "How: Lie back on a bench set to an incline angle and lift a barbell to shoulder height, palms facing away from you. Breathe out as you press up with both arms. Lock out your arms and squeeze your chest before returning slowly to the start position.", "null"))
        //arrayList.add("Incline barbell bench press")
        addExercise( Exercise("Incline dumbbell bench press","https://www.youtube.com/watch?time_continue=1&v=qSmo-8QapTg&feature=emb_title", "0", "null","Why: While the bench press is the chest-bulking staple, dumbbell presses offer different variants to help you build a bigger chest. By using two separate weights, you have a greater range of motion, while activating more stabilising muscles too.\n" +
                "\n" +
                "To make it harder, Take the weight all the way to the top of the movement before lowering the opposite side to keep your chest under tension.\n" +
                "\n" +
                "How: Lie back on a bench set to a 45-degree angle and lift the weights over your chest, palms facing away from you. Slowly lower one weight, then drive it back up and squeeze your chest at the top.Repeat with the other side.", "null"))
        //arrayList.add("Incline dumbbell bench press")
        addExercise( Exercise("Close-grip barbell bench press","https://www.youtube.com/watch?v=G6weXb6aeUc", "0", "null","Why: The close-grip bench press places less strain on your shoulders, shifting the emphasis to your triceps and chest. Place your hands just inside of shoulder width.\n" +
                "\n" +
                "How: Lie back on a flat bench holding a barbell with a narrow, overhand grip. From the starting position, breathe in and lower the bar slowly until it skims the middle of your chest. Push the bar back to the starting position explosively as you breathe out. Focus on pushing the bar using your chest muscles.", "null"))
        //arrayList.add("Close-grip barbell bench press")
        addExercise( Exercise("Decline press-up","https://www.youtube.com/watch?v=X92uGqa1eeU", "0", "null","Why: Decline bench press-ups place the muscle-building emphasis on your lower pectorals, helping you build a well-rounded and more defined chest.\n" +
                "\n" +
                "How: Place your feet on a bench with your hands planted on the floor in front of you. Lower your body down until your chest almost reaches the floor. Press your body back up to the starting position while squeezing your chest. Pause briefly at the top before repeating.", "null"))
        //arrayList.add("Decline press-up")
        addExercise( Exercise("Cable fly","https://www.youtube.com/watch?v=Iwe6AmxVf7o", "0", "null","Why: Give your pectorals and deltoids a new stimulus instead of pressing. Add the cable fly to your chest day to provide constant tension throughout the full movement.\n" +
                "\n" +
                "How: Attach stirrup handles to the high pulleys of a cable crossover machine. Take one in each hand – your arms should be outstretched with a slight bend. Place one foot slightly forward, brace your core, and pull the handles downward and across your body. Return to the start position under control", "null"))
        //arrayList.add("Cable fly")
        addExercise( Exercise("Decline barbell bench press","https://www.youtube.com/watch?v=iVh4B5bJ5OI&feature=emb_title", "0", "null","Why: Using a decline bench helps zero-in on your lower chest, helping you build serious size on your chest.\n" +
                "\n" +
                "How: Holding a barbell with your hands shoulder-width apart and palms facing your feet, lie back on a bench fixed to a decline setting. Start with your arms fully extended and hands over your chest, then lower the bar slowly until it skims the middle of your chest. Push the barbell back to the starting position explosively as you breathe out.", "null"))
        //arrayList.add("Decline barbell bench press")
        addExercise( Exercise("Decline Dumbbell Bench Press","https://www.youtube.com/watch?v=0xRvl4Qv3ZY&feature=emb_title", "0", "null","", "null"))
        //arrayList.add("Decline Dumbbell Bench Press")
        addExercise( Exercise("Staggered press-up","https://www.youtube.com/watch?v=DWxxFKepmko", "0", "null","Why: If you've already mastered press-ups, then this is an ideal stepping-stone into mastering the one-handed press-up.\n" +
                "\n" +
                "How: Get into a press-up position with your hands staggered, so your right is further forward then the left. Lower your body until your chest is an inch from the ground then drive up explosively. Pull your hands off the floor and switch positions so your left leads, then repeat.", "null"))
        //arrayList.add("Staggered press-up")
        addExercise( Exercise("Chest dips","https://www.youtube.com/watch?v=4la6BkUBLgo", "0", "null","Why: If you want to burn chest fat and lose your man boobs, dips are one of the best ways to go about it. Working the entire upper-body, dips also work your arms, shoulders and upper back. As you descend, you'll be fighting to stabilise your entire body as it moves through space, giving your body a huge hormonal boost.\n" +
                "\n" +
                "How: Grab the bars of a dip station with your palms facing inward and your arms straight. Slowly lower until your elbows are at right angles, ensuring they stay tucked against your body and don't flare out. Drive yourself back up to the top and repeat.", "null"))
        //arrayList.add("Chest dips")
        addExercise( Exercise("Clap press-up","https://www.youtube.com/watch?v=EYwWCgM198U", "0", "null","Why: Scorch your fast-twitch muscle fibres and prime them for growth with this plyometric take on the bodyweight classic\n" +
                "\n" +
                "How: Get into a push-up position, your hands just outside your chest, your feet shoulder-width apart, and your body forming a straight line from head to heels. Brace your core. Lower your chest to the floor and then press up explosively so your hands come off the floor, clapping once at the top of the movement.", "null"))
        //arrayList.add("Clap press-up")
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
