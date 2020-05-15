package com.stayfit.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.stayfit.R
import com.stayfit.config.AppPrefs
import com.stayfit.hide
import com.stayfit.show
import com.stayfit.ui.login.LoginActivity
import com.stayfit.ui.onboarding.SliderAdapter
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.register_slide1.*
import kotlinx.android.synthetic.main.register_slide1.tv_username


class SignUpActivity2 : AppCompatActivity() {

    private val TAG = "SignUpActivity2"

    private lateinit var mAuth: FirebaseAuth

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance();

    private lateinit var sliderAdapter: SliderAdapter
    private var dots: Array<TextView?>? = null
    private lateinit var layouts: Array<Int>
    private val sliderChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            addBottomDots(position)

            if (position == layouts.size.minus(1)) {
                startBtn.show()
            } else {
                startBtn.hide()
            }
        }

        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            Log.d(TAG, "onPageScrolled: " + tv_username.text.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        mAuth = FirebaseAuth.getInstance()

        init()
        dataSet()
        interactions()
    }

    private fun init() {
        /** Layouts of the three onBoarding Screens.
         *  Add more layouts if you wish.
         **/
        layouts = arrayOf(
            R.layout.register_slide1,
            R.layout.register_slide2,
            R.layout.register_slide3
        )

        sliderAdapter = SliderAdapter(this, layouts)
    }

    private fun dataSet() {
        /**
         * Adding bottom dots
         * */
        addBottomDots(0)

        slider.apply {
            adapter = sliderAdapter
            addOnPageChangeListener(sliderChangeListener)
        }
    }

    private fun interactions() {
        startBtn.setOnClickListener {
            // Launch login screen
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        AppPrefs(this).setFirstTimeLaunch(false)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts.size)

        dotsLayout.removeAllViews()
        for (i in dots!!.indices) {
            dots!![i] = TextView(this)
            dots!![i]?.text = Html.fromHtml("&#8226;")
            dots!![i]?.textSize = 35f
            dots!![i]?.setTextColor(resources.getColor(R.color.colorGrey))
            dotsLayout.addView(dots!![i])
        }

        if (dots!!.isNotEmpty()) {
            dots!![currentPage]?.setTextColor(resources.getColor(R.color.colorIndigo))
        }
    }

    private fun getCurrentScreen(i: Int): Int = slider.currentItem.plus(i)
}
