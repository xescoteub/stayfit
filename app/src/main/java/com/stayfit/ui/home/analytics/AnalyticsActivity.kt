package com.stayfit.ui.home.analytics

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.stayfit.R
import kotlinx.android.synthetic.main.activity_analytics.*
import kotlinx.android.synthetic.main.bottom_sheet_calories.*
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
        // Calories bottom sheet
        // ====================================================================

        // Init the bottom sheet view
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetCalories)

        /**
         * Toggles calories bottom sheet
         */
        caloriesCard.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        // ====================================================================
        // Charts
        // ====================================================================
        val circularProgress = findViewById<CircularProgressIndicator>(R.id.circular_progress)
        circularProgress.setProgress(5000.0, 10000.0);

        val circularProgress2 = findViewById<CircularProgressIndicator>(R.id.circular_progress2)
        circularProgress2.setProgress(345.0, 1000.0);

        //setBarChart()
    }

    private fun setBarChart() {
        /*val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(8f, 0))
        entries.add(BarEntry(2f, 1))
        entries.add(BarEntry(5f, 2))
        entries.add(BarEntry(20f, 3))
        entries.add(BarEntry(15f, 4))
        entries.add(BarEntry(19f, 5))

        val barDataSet = BarDataSet(entries, "Cells")

        val labels = ArrayList<String>()
        labels.add("18-Jan")
        labels.add("19-Jan")
        labels.add("20-Jan")
        labels.add("21-Jan")
        labels.add("22-Jan")
        labels.add("23-Jan")
        val data = BarData(labels, barDataSet)
        //barChart.data = data // set the data and list of lables into chart

        //barChart.setDescription("Set Bar Chart Description")  // set the description

        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
        barDataSet.color = resources.getColor(R.color.colorIndigo)

        //barChart.animateY(5000)*/
    }
}
