package com.stayfit.ui.home.analytics

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import com.stayfit.R
import com.stayfit.ui.home.blogs.Blog
import kotlinx.android.synthetic.main.activity_analytics.*
import kotlinx.android.synthetic.main.bottom_sheet_calories.*
import kotlinx.android.synthetic.main.bottom_sheet_heart.*
import kotlinx.android.synthetic.main.bottom_sheet_sleep.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

/**
 *
 */
class AnalyticsActivity : AppCompatActivity() {

    private val TAG = "AnalyticsActivity"

    var baseURL = "https://us-central1-stayfit-87c1a.cloudfunctions.net"

    private val FIREBASE_CLOUD_FUNCTION_BASE_URL = "$baseURL/api"

    private lateinit var mAuth: FirebaseAuth

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_analytics);

        mAuth = FirebaseAuth.getInstance()

        // ====================================================================
        // Fetch user analytics
        // ====================================================================
        fetchUserAnalytics()

        // ====================================================================
        // Toolbar config
        // ====================================================================
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.app_name)

        toolbar.setNavigationOnClickListener { v: View? -> onBackPressed() }

        // ====================================================================
        // Sleep bottom sheet
        // ====================================================================

        // Init the bottom sheet view
        val sleepBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetSleep)

        /**
         * Toggles sleep bottom sheet
         */
        sleepCard.setOnClickListener {
            if (sleepBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                sleepBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                sleepBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        // ====================================================================
        // Calories bottom sheet
        // ====================================================================

        // Init the bottom sheet view
        val caloriesBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetCalories)

        /**
         * Toggles calories bottom sheet
         */
        caloriesCard.setOnClickListener {
            if (caloriesBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                caloriesBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                caloriesBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        // ====================================================================
        // Heart bottom sheet
        // ====================================================================

        // Init the bottom sheet view
        val heartBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetHeart)

        /**
         * Toggles heart bottom sheet
         */
        heartCard.setOnClickListener {
            if (heartBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                heartBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                heartBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        // ====================================================================
        // Charts
        // ====================================================================
        val stepsChart = findViewById<CircularProgressIndicator>(R.id.stepsChart)
        stepsChart.setProgress(5321.0, 10000.0);
    }

    private fun fetchUserAnalytics() {
        Log.d(TAG, "fetchUserAnalytics")

        progress_circular.visibility = View.VISIBLE;

        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("$FIREBASE_CLOUD_FUNCTION_BASE_URL/analytics/user/${mAuth.uid}")
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(request: Request?, e: IOException?) {

            }

            override fun onResponse(response: Response?) {
                val response: String = response?.body()!!.string()
                this@AnalyticsActivity.runOnUiThread(Runnable {
                    try {
                        Log.d(TAG, "<json> $response </json>")
                        parseAnalyticsJSON(JSONObject(response))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } finally {
                        progress_circular.visibility = View.GONE;
                    }
                })
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun parseAnalyticsJSON(json: JSONObject)
    {
        val caloriesChart = findViewById<CircularProgressIndicator>(R.id.caloriesChart)

        Log.d(TAG, "[total_time]:" + json["total_time"])

        val total_time: String = json["total_time"].toString()
        val bmi: String = json["bmi"].toString()
        val calories_burned1 : String = json["calories_burned"] as String
        val calories_burned2: Double = calories_burned1.toDouble()

        tv_gym_time.text        = "$total_time min"
        tv_body_mass.text       = bmi
        caloriesChart.setProgress(calories_burned2, 2500.0)
    }
}
