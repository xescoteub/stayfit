package com.stayfit.ui.home.analytics

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.stayfit.R
import kotlinx.android.synthetic.main.activity_analytics.*
import kotlinx.android.synthetic.main.bottom_sheet_calories.*
import kotlinx.android.synthetic.main.bottom_sheet_heart.*
import kotlinx.android.synthetic.main.bottom_sheet_sleep.*
import java.util.*

/**
 *
 */
class AnalyticsActivity : AppCompatActivity() {

    private val TAG = "AnalyticsActivity"

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_analytics);

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

        val caloriesChart = findViewById<CircularProgressIndicator>(R.id.caloriesChart)
        caloriesChart.setProgress(345.0, 1000.0);


        // ====================================================================
        // Heart chart
        // ====================================================================
        /*
        val graph = findViewById<View>(R.id.heartChart) as GraphView
        val series = LineGraphSeries<DataPoint>()

        var x = -0.5
        var y = 0.0;

        for (i in 0..500)
        {
            x += 0.1
            y = Math.sin(x)
            series.appendData(DataPoint(x,y), true, 500)
        }
        graph.addSeries(series)
        */
    }
}
