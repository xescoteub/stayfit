package com.stayfit.ui.home.exercises

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.StrictMode
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore
import com.stayfit.R
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import java.util.*
import kotlin.collections.ArrayList

class DailyExercisesActivity : AppCompatActivity() {

    private val TAG = "DailyExercisesActivity"

    var sdk = Build.VERSION.SDK_INT

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance();

    lateinit var exercisesList: ArrayList<Exercise>

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
    // Exercises progress indicator
    // ====================================================
    /**
     * The exercises list indicator layout
     */
    var indicatorLayout: LinearLayout? = null

    var rowTextView: TextView? = null

    var rootPos = 0

    lateinit var myTextViews: Array<TextView?>


    // ====================================================
    // Texts
    // ====================================================
    var txt_header: TextView? = null

    var txt_done: TextView? = null

    var txt_sec: TextView? = null

    // ====================================================
    // The progress indicator
    // ====================================================

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
        fetchExercises()

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

        lv_View             = findViewById(R.id.lv_view)

        final_View          = findViewById(R.id.fianl_view)

        btn_home            = findViewById(R.id.btn_home)

        imageView           = findViewById(R.id.workoutImageView)

        indicatorLayout     = findViewById(R.id.exerciseIndicator)

        txt_header          = findViewById(R.id.header)

        txt_done            = findViewById(R.id.text_done)

        txt_sec             = findViewById(R.id.sec)

        lv_done             = findViewById(R.id.done)

        lv_timer            = findViewById(R.id.timer)

        skip                = resources.getString(R.string.skip)
        take_rest           = resources.getString(R.string.take_rest)
        done                = resources.getString(R.string.Done)

        txt_done!!.setText(done)
        val c = Calendar.getInstance()
        val day = c[Calendar.DAY_OF_MONTH]
        val month = c[Calendar.MONTH]
        val year = c[Calendar.YEAR]
        current_date = day.toString() + "/" + (month + 1) + "/" + year
    }

    /**
     *
     */
    private fun fetchExercises()
    {
        db.collection("exercises")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    val exerciseObj = document.data as HashMap<*, *>
                    val exercise = Exercise()

                    with(exercise) {
                        name    = exerciseObj["name"].toString()
                        desc    = exerciseObj["desc"].toString()
                        images  = exerciseObj["images"] as ArrayList<String>?
                    }
                    Log.d(TAG, "exercise: $exercise")
                    exercisesList.add(exercise)

                    // Initialize exercises progress indicator
                    // with the exercises list size
                    myTextViews = arrayOfNulls(exercisesList.size)
                }

                // Display first exercise form list
                displayData(currentExercise)

                createTextPosition()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }


    @SuppressLint("SetTextI18n")
    private fun createTextPosition()
    {
        Log.d(TAG, "createTextPosition $exercisesList")
        Log.d(TAG, "exercisesList indices ${exercisesList.indices}")

        // Iteration without extra object allocations
        for (idx in exercisesList.indices) {

            rowTextView = TextView(this)
            rootPos = idx
            rowTextView!!.text = "" + (idx + 1)
            rowTextView!!.gravity = Gravity.CENTER
            rowTextView!!.setTextColor(Color.BLACK)

            val lp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)

            lp.weight = 1f
            rowTextView!!.layoutParams = lp
            rowTextView!!.setPadding(0, 0, 15, 0)
            rowTextView!!.textSize = 12f

            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                rowTextView!!.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_circle_background_grey))
            } else {
                rowTextView!!.background = resources.getDrawable(R.drawable.ic_circle_background_grey)
            }
            indicatorLayout!!.addView(rowTextView)
            myTextViews[idx] = rowTextView

        }

        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            myTextViews[currentExercise]
                ?.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_circle_background_green))
        } else {
            myTextViews[currentExercise]?.background = resources.getDrawable(R.drawable.ic_circle_background_green)
        }
        myTextViews[currentExercise]!!.setTextColor(Color.WHITE)
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
     * Display running exercise data
     */
    private fun displayData(position: Int)
    {
        Log.d(TAG, "displayData $position")

        val images = exercisesList[position].images;
        Log.d(TAG, "images $images")

        // Set exercise image
        imageView!!.setImageBitmap(getImageBitmap(images!![0]))

        // Set exercise description header
        txt_header!!.text = exercisesList[position].desc;
        println("txt_header: " + exercisesList[position].desc)

        /*id      = model_data!![position].id
        imgPath = model_data!![position].image + ".png"
        ref_id  = model_data!![position].ref_i*/
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
