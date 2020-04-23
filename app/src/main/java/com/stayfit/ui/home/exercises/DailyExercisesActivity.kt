package com.stayfit.ui.home.exercises

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.*
import com.stayfit.R
import com.stayfit.ui.home.feed.Blog
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import java.util.*

class DailyExercisesActivity : AppCompatActivity() {

    private val TAG = "DailyExercisesActivity"

    lateinit var exercisesList: ArrayList<Exercise>

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    val exercisesRef: DatabaseReference = database.getReference("exercises")

    /**
     * The list of exercises
     */
    //var model_data: List<ExerciseModel>? = null

    // ====================================================
    //
    // ====================================================
    var lv_View: LinearLayout? = null

    var final_View: LinearLayout? = null

    // ====================================================
    //
    // ====================================================
    var btn_home: TextView? = null

    var skip: String? = null

    var take_rest: String? = null

    var done: String? = null

    /**
     * The currently running exercise image
     */
    var imageView: ImageView? = null

    // ====================================================
    // Texts
    // ====================================================
    var txt_header: TextView? = null

    var txt_done: TextView? = null

    var txt_sec: TextView? = null

    // ====================================================
    // Labels
    // ====================================================
    var lv_done: RelativeLayout? = null

    var lv_timer: RelativeLayout? = null


    // ====================================================
    // Timer
    // ====================================================
    var milliseconds = 0

    var countDownTimer: CountDownTimer? = null

    var restTimer: CountDownTimer? = null

    var current_date: String? = null


    /**
     * The current exercise from list
     */
    var currentExercise = 0


    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_daily_exercices)

        val pref = applicationContext.getSharedPreferences(
            "MyPrefs",
            Context.MODE_PRIVATE
        )

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        // Initialize exercises array list
        exercisesList = ArrayList()

        // Initialize layout
        init()

        // Fetch exercises list
        fetchExercises();

        milliseconds = pref.getInt("seconds", 10000)

        countDownTimer = object : CountDownTimer(300, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                countDownTimer!!.start()

            }
        }.start()

        /**
         * Display the countdown timer
         */
        lv_done!!.setOnClickListener { v: View? ->
            setTimerLayout()

            onDoneClicked()
        }
    }

    /**
     *
     */
    private fun init()
    {
        //model_data      = ArrayList()
        //get_ref_data    = ArrayList()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setTitle(resources.getString(R.string.app_name))
        toolbar.setNavigationOnClickListener { v: View? -> onBackPressed() }

        lv_View     = findViewById(R.id.lv_view)

        final_View  = findViewById(R.id.fianl_view)

        btn_home    = findViewById(R.id.btn_home)

        imageView   = findViewById(R.id.workoutImageView)

        txt_header  = findViewById(R.id.header)

        txt_done    = findViewById(R.id.text_done)

        txt_sec     = findViewById(R.id.sec)

        lv_done     = findViewById(R.id.done)

        lv_timer    = findViewById(R.id.timer)

        skip        = resources.getString(R.string.skip)
        take_rest   = resources.getString(R.string.take_rest)
        done        = resources.getString(R.string.Done)

        txt_done!!.setText(done)
        val c = Calendar.getInstance()
        val day = c[Calendar.DAY_OF_MONTH]
        val month = c[Calendar.MONTH]
        val year = c[Calendar.YEAR]
        current_date = day.toString() + "/" + (month + 1) + "/" + year
    }

    private fun fetchExercises()
    {
        // Read from the database
        exercisesRef.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val map = dataSnapshot.getValue()
                Log.d(TAG, "map: ${map}")

                dataSnapshot.children.forEach {
                    var exerciseObj = it.getValue() as HashMap<*, *>
                    var exercise = Exercise()

                    with(exercise) {
                        name    = exerciseObj["name"].toString()
                        desc    = exerciseObj["desc"].toString()
                        image   = exerciseObj["image"].toString()
                    }
                    Log.d(TAG, "exercise: ${exercise}")
                    exercisesList.add(exercise)
                }

                // Display first exercise form list
                displayData(currentExercise)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        });
    }

    /**
     *
     */
    private fun setTimerLayout()
    {
        Log.d(TAG, "setLayout")
        txt_header!!.text = take_rest
        txt_done!!.text = skip
        countDownTimer!!.cancel()
        imageView!!.visibility = View.GONE
        lv_timer!!.visibility = View.VISIBLE
    }

    private fun onDoneClicked() {
        Log.d(TAG, "onDoneClicked")

        restTimer = object : CountDownTimer(milliseconds.toLong(), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                Log.d(TAG, "onTick")

                txt_sec!!.text = "" + millisUntilFinished / 1000

                Log.d(TAG, "onTick: ${txt_sec}")

            }

            override fun onFinish() {
                Log.d(TAG, "onFinish")

                restTimer!!.cancel()
                imageView!!.visibility = View.VISIBLE
                txt_done!!.text = done
                lv_timer!!.visibility = View.GONE

                countDownTimer!!.start()
                //ref_pos = 0
                restTimer!!.cancel()
            }
        }
    }


    /**
     *
     */
    private fun displayData(position: Int)
    {
        Log.d(TAG, "displayData $position")

        // It could be nullable, make it null safe adding ?
        //holder.blogName?.text = items.get(position).name
        //holder.blogDesc?.text = items.get(position).description
        val url = exercisesList[position].image;
        Log.d(TAG, "url $url")

        // Set exercise image
        imageView!!.setImageBitmap(getImageBitmap(url))


        /*id      = model_data!![position].id
        imgPath = model_data!![position].image + ".png"
        ref_id  = model_data!![position].ref_id

        imageView!!.setImageBitmap(getBitmapFromAsset(imgPath!!))
        exercise_type = if (work_type == 0) {
            model_data!![position].easy
        } else if (work_type == 1) {
            model_data!![position].medium
        } else {
            model_data!![position].hard
        }
        txt_header!!.text = exercise_type*/
    }

    /**
     * Get bitmap from URL
     */
    private fun getImageBitmap(url: String): Bitmap?
    {
        var bm: Bitmap? = null
        try {
            val aURL = URL(url)
            val conn: URLConnection = aURL.openConnection()
            conn.connect()
            val `is`: InputStream = conn.getInputStream()
            val bis = BufferedInputStream(`is`)
            bm = BitmapFactory.decodeStream(bis)
            bis.close()
            `is`.close()
        } catch (e: IOException) {
            Log.e("BlogAdapter", "Error getting bitmap", e)
        }
        return bm
    }
}
