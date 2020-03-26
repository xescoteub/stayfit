package com.stayfit.ui.workouts.exercises.abs

import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.VideoView
import com.stayfit.R
import kotlin.math.roundToInt

class AssistedReverseSideSetup : AppCompatActivity() {
    var progressBar: ProgressBar? = null
    var myCountDownTimer: MyCountDownTimer? = null
    private var delay:Delay = Delay(3000, 1000)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assisted_reverse_side_setup)
        progressBar = findViewById<ProgressBar>(R.id.progressBar_assistedreversesidesetup)
        //progressBar!!.getProgressDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar!!.getProgressDrawable().setColorFilter(Color.parseColor("#233475"), android.graphics.PorterDuff.Mode.SRC_IN);
        downProgressBar(15000) //10s
    }
    private fun downProgressBar(durada: Long){
        myCountDownTimer = MyCountDownTimer(durada, 1000)
        progressBar!!.max=(durada / 1000).toInt()
        progressBar!!.progress=progressBar!!.max
        delay.start()
        myCountDownTimer!!.start()
    }

    inner class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            progressBar!!.progress =  (millisUntilFinished / 1000).toInt()
        }
        override fun onFinish() {
            finish()
        }
    }
    inner class Delay(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
        }
        override fun onFinish() {
        }
    }
}
