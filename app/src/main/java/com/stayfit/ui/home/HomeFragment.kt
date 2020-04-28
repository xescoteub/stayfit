package com.stayfit.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stayfit.R
import com.stayfit.ui.home.analytics.AnalyticsActivity
import com.stayfit.ui.home.exercises.DailyExercisesActivity
import com.stayfit.ui.home.feed.BlogsActivity
import kotlinx.android.synthetic.main.home_fragment.*

/**
 *
 */
class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: HomeFragment")
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /**
         * Start daily exercises activity when start button is clicked
         */
        startExercisesButton.setOnClickListener {
            val intent = Intent(activity, DailyExercisesActivity::class.java)
            startActivity(intent)
        }

        /**
         * Start feed activity when blogs button is clicked
         */
        blogsButton.setOnClickListener {
            val intent = Intent(activity, BlogsActivity::class.java)
            startActivity(intent)
        }

        /**
         * Start analytics activity when analytics button is clicked
         */
        analyticsButton.setOnClickListener {
            val intent = Intent(activity, AnalyticsActivity::class.java)
            startActivity(intent)
        }
    }
}
