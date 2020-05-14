package com.stayfit.ui.myroutines

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ProgressBar
import android.widget.TextView
import com.stayfit.R
import com.stayfit.ui.workouts.exercises.ExerciseActivity
import kotlinx.android.synthetic.main.activity_rest_time.*

class RestTimeActivity : AppCompatActivity() {
    var progressBar: ProgressBar? = null
    var myCountDownTimer: RestTimeActivity.MyCountDownTimer? = null
    var text_time: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest_time)
        var time: Int = intent.getIntExtra("delay_time",20)
        progressBar = findViewById<ProgressBar>(R.id.progressBar_delay)
        text_time = findViewById<TextView>(R.id.txt_time_delay)
        progressBar!!.getProgressDrawable().setColorFilter(Color.parseColor("#6c63ff"), android.graphics.PorterDuff.Mode.SRC_IN);
        var t: Long = (time*1000).toLong() // convert seconds to ms
        downProgressBar(t)
    }

    private fun downProgressBar(durada: Long){
        myCountDownTimer = MyCountDownTimer(durada, 1000)
        progressBar!!.max=(durada / 1000).toInt()
        progressBar!!.progress=progressBar!!.max
        text_time!!.setText(progressBar!!.progress.toString()+'"')
        myCountDownTimer!!.start()
    }

    inner class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            progressBar!!.progress =  (millisUntilFinished / 1000).toInt()
            text_time!!.setText(progressBar!!.progress.toString()+'"'+"/"+progressBar!!.max.toString()+'"')
        }
        override fun onFinish() {
            finish()
        }
    }
}
