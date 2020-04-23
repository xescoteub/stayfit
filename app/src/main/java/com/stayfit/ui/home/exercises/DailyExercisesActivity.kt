package com.stayfit.ui.home.exercises

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.stayfit.R
import java.util.*

class DailyExercisesActivity : AppCompatActivity() {

    private val TAG = "DailyExercisesActivity"

    var lv_View: LinearLayout? = null

    /**
     * The currently running exercise image
     */
    var imageView: ImageView? = null

    var txt_header: TextView? = null

    var txt_done: TextView? = null

    var txt_sec: TextView? = null

    var lv_done: RelativeLayout? = null

    var lv_timer: RelativeLayout? = null

    var milliseconds = 0

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

        milliseconds = pref.getInt("seconds", 10000)

        init()

    }



    private fun init()
    {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setTitle(resources.getString(R.string.app_name))
        toolbar.setNavigationOnClickListener { v: View? -> onBackPressed() }

        lv_View = findViewById(R.id.lv_view)

        imageView = findViewById(R.id.workoutImageView)

        txt_header = findViewById(R.id.header)

        txt_done = findViewById(R.id.text_done)

        txt_sec = findViewById(R.id.sec)

        lv_done = findViewById(R.id.done)

        lv_timer = findViewById(R.id.timer)
    }


    /*
     private fun init() {
        model_data = ArrayList()
        get_ref_data = ArrayList()
        pass_id = ArrayList()
        history_data = ArrayList()
        upDateData = ArrayList()
        check_date = ArrayList()
        val toolbar =
            findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setTitle(resources.getString(R.string.app_name))
        toolbar.setNavigationOnClickListener { v: View? -> onBackPressed() }
        imageView = findViewById(R.id.imaegview)
        txt_header = findViewById(R.id.header)
        txt_done = findViewById(R.id.text_done)
        txt_sec = findViewById(R.id.sec)
        lv_done = findViewById(R.id.done)
        lv_timer = findViewById(R.id.timer)
        lv_View = findViewById(R.id.lv_view)
        final_View = findViewById(R.id.fianl_view)
        indicator_Layout = findViewById(R.id.indiactor)
        btn_home = findViewById(R.id.btn_home)
        adView = findViewById(R.id.adView)
        val adRequest2 = AdRequest.Builder().build()
        adView!!.loadAd(adRequest2)
        //        txt_header.setText(type_array[position]);
        skip = resources.getString(R.string.Skip)
        take_rest = resources.getString(R.string.take_rest)
        done = resources.getString(R.string.Done)
        txt_done!!.setText(done)
        val c = Calendar.getInstance()
        val day = c[Calendar.DAY_OF_MONTH]
        val month = c[Calendar.MONTH]
        val year = c[Calendar.YEAR]
        current_date = day.toString() + "/" + (month + 1) + "/" + year
    }
     */
}
