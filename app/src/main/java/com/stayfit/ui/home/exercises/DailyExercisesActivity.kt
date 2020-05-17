package com.stayfit.ui.home.exercises

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.stayfit.MainActivity
import com.stayfit.R
import com.stayfit.config.BaseHTTPAction
import kotlinx.android.synthetic.main.activity_daily_exercices.*
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import java.util.*
import kotlin.collections.ArrayList

class DailyExercisesActivity : BaseHTTPAction() {

    private val TAG = "DailyExercisesActivity"

    var sdk = Build.VERSION.SDK_INT

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance();

    private lateinit var mAuth: FirebaseAuth

    private val FIREBASE_CLOUD_FUNCTION_BASE_URL = "$baseURL/api"

    /**
     * The list of exercises
     */
    lateinit var exercisesList: ArrayList<Exercise>

    // ====================================================
    //
    // ====================================================
    private var mainView: LinearLayout? = null

    private var finalExercisesView: LinearLayout? = null

    // ====================================================
    //
    // ====================================================
    private var buttonHome: TextView? = null

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

    lateinit var myTextViews: Array<TextView?>


    // ====================================================
    // Texts
    // ====================================================
    private var textHeader: TextView? = null

    var textSecondsCounter: TextView? = null

    // ====================================================
    // The progress indicator
    // ====================================================

    // ====================================================
    // Labels
    // ====================================================
    var lv_timer: RelativeLayout? = null


    // ====================================================
    // Timer
    // ====================================================
    var milliseconds = 0

    var countDownTimer: CountDownTimer? = null

    var restTimer: CountDownTimer? = null

    /**
     * The current exercise from list
     */
    var currentExercise = 0

    // TODO: explain
    var isDoneClicked : Boolean= false

    var totalSeconds : ArrayList<Int> = ArrayList()

    /**
     *
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_daily_exercices)

        val pref = applicationContext.getSharedPreferences(
            "MyPrefs",
            Context.MODE_PRIVATE
        )

        // Creates ThreadPolicy instances.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // Initialize exercises array list
        exercisesList = ArrayList()

        // Initialize layout
        init()

        // Fetch exercises list
        fetchExercises()

        // 60 seconds workout countdown time
        milliseconds = pref.getInt("seconds", 60000)
        println(">>>>>>>>>>>>>>>>>>>>> milliseconds" + milliseconds)
        countDownTimer = object : CountDownTimer(300, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                countDownTimer!!.start()
            }
        }.start()

        /**
         *
         */
        done_button.setOnClickListener {
            if (currentExercise < exercisesList.size - 1) {
                if (!isDoneClicked) {
                    isDoneClicked = true

                    /**
                     * Displays timer countdown layout
                     */
                    showTimerLayout()

                    Log.e("id==", "" + currentExercise)

                    doneClicked()
                } else {
                    isDoneClicked = false
                    skipClicked()
                }
            } else {
                mainView!!.visibility = View.GONE
                finalExercisesView!!.visibility = View.VISIBLE

                /**
                 * Go to home view
                 */
                buttonHome!!.setOnClickListener {
                    startActivity(Intent(this, MainActivity::class.java))
                    insertActivity()
                }
            }
        }
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    private fun init()
    {
        // Toolbar config
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.app_name)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        mainView                = findViewById(R.id.lv_view)

        finalExercisesView      = findViewById(R.id.exercises_final_view)

        buttonHome              = findViewById(R.id.button_go_home)

        imageView               = findViewById(R.id.workout_image_view)

        indicatorLayout         = findViewById(R.id.exerciseIndicator)

        textHeader              = findViewById(R.id.header)

        // =====================================================
        // Timer container
        // =====================================================
        lv_timer                = findViewById(R.id.timer_container)

        textSecondsCounter      = findViewById(R.id.timer_seconds)

        done_button.text        = resources.getString(R.string.Start)
    }

    /**
     * Fetch exercises list
     */
    private fun fetchExercises()
    {
        progress_circular.visibility = View.VISIBLE;

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
                progress_circular.visibility = View.GONE;

                createExercisesProgressLayout()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    private fun createExercisesProgressLayout()
    {
        Log.d(TAG, "exercisesList indices ${exercisesList.indices}")

        // Iteration without extra object allocations
        for (idx in exercisesList.indices) {

            rowTextView = TextView(this)
            rowTextView!!.text = "" + (idx + 1)
            rowTextView!!.gravity = Gravity.CENTER
            rowTextView!!.setTextColor(Color.BLACK)

            val lp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.weight = 1f

            rowTextView!!.layoutParams = lp
            rowTextView!!.setPadding(0, 0, 15, 0)
            rowTextView!!.textSize = 12f

            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                rowTextView!!.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_circle_background_flat))
            } else {
                rowTextView!!.background = resources.getDrawable(R.drawable.ic_circle_background_flat)
            }
            indicatorLayout!!.addView(rowTextView)
            myTextViews[idx] = rowTextView
        }

        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            myTextViews[currentExercise]?.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_circle_background_filled))
        } else {
            myTextViews[currentExercise]?.background = resources.getDrawable(R.drawable.ic_circle_background_filled)
        }
        myTextViews[currentExercise]!!.setTextColor(Color.WHITE)
    }

    /**
     *
     */
    private fun showTimerLayout()
    {
        Log.d(TAG, "setLayout")
        done_button.text =  resources.getString(R.string.skip)

        countDownTimer!!.cancel()

        imageView!!.visibility = View.GONE
        lv_timer!!.visibility = View.VISIBLE
    }

    /**
     *
     */
    private fun doneClicked()
    {
        restTimer = object : CountDownTimer(milliseconds.toLong(), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                textSecondsCounter!!.text = "" + millisUntilFinished / 1000 + "s"
            }

            override fun onFinish() {
                isDoneClicked = false
                restTimer!!.cancel()
                imageView!!.visibility = View.VISIBLE

                done_button.text = resources.getString(R.string.Start)

                lv_timer!!.visibility = View.GONE

                if (currentExercise < exercisesList.size) {
                    currentExercise += 1

                    displayData(currentExercise)

                    for (i in myTextViews.indices) {
                        if (currentExercise == i) {
                            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                                myTextViews[currentExercise]?.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_circle_background_filled))
                            } else {
                                myTextViews[currentExercise]?.setBackground(resources.getDrawable(R.drawable.ic_circle_background_filled))
                            }
                            myTextViews[i]!!.setTextColor(Color.WHITE)
                        }
                    }
                    countDownTimer!!.start()
                    restTimer!!.cancel()
                }
            }
        }.start()
    }

    /**
     *
     */
    private fun skipClicked()
    {
        restTimer!!.cancel()
        imageView!!.visibility = View.VISIBLE

        done_button.text = resources.getString(R.string.Start)

        lv_timer!!.visibility = View.GONE

        if (currentExercise < exercisesList.size) {
            currentExercise += 1

            displayData(currentExercise)

            for (i in myTextViews.indices) {
                if (currentExercise == i) {
                    if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                        myTextViews[currentExercise]?.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_circle_background_filled))
                    } else {
                        myTextViews[currentExercise]?.background = resources.getDrawable(R.drawable.ic_circle_background_filled)
                    }
                    myTextViews[i]!!.setTextColor(Color.WHITE)
                }
            }
            val secs = removeLastChar(textSecondsCounter?.text.toString())?.toInt()

            if (secs != null) {
                val elapsed = (milliseconds / 1000) - (secs)
                Log.d(TAG, ">>>>> elapsed:" + elapsed)

                totalSeconds.add(elapsed)
            }
            Log.d(TAG, ">>>>> totalSeconds: $totalSeconds")


            countDownTimer!!.start()
            restTimer!!.cancel()
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
        imageView!!.setImageBitmap(__getImageBitmap(images!![0]))

        // Set exercise description header
        textHeader!!.text = exercisesList[position].desc;
    }

    /**
     * Get bitmap from URL
     */
    private fun __getImageBitmap(url: String): Bitmap?
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

    override fun responseRunnable(response: String?): Runnable? {
        TODO("Not yet implemented")
    }

    /**
     * Insert new daily activity entry
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertActivity()
    {
        // The sum of time spent for each exercise
        val time = totalSeconds.sum()
        Log.d(TAG, "<time> $time </time>")

        val docData = hashMapOf(
            "user_id" to mAuth.currentUser!!.uid,
            "date" to Date().toString(),
            "total_time" to time
        )

        db.collection("daily_exercises")
            .add(docData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }

    private fun removeLastChar(str: String): String? {
        return str.substring(0, str.length - 1)
    }
}