package com.stayfit.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stayfit.R
import com.stayfit.ui.home.exercises.DailyExercisesActivity
import com.stayfit.ui.home.feed.FeedActivity
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
            val intent = Intent(activity, FeedActivity::class.java)
            startActivity(intent)
        }
    }
}
