package com.stayfit.ui.workouts.exercises.abs

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.VideoView
import com.stayfit.R

class AssistedReverseSideSetup : AppCompatActivity() {
    var progressBar: ProgressBar? = null
    var myCountDownTimer: MyCountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assisted_reverse_side_setup)
        progressBar = findViewById(R.id.progressBar_assistedreversesidesetup) as ProgressBar?
        myCountDownTimer = MyCountDownTimer(10000, 1000)
        myCountDownTimer!!.start()

    }
    inner class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            val progress = (millisUntilFinished / 1000).toInt()
            progressBar!!.progress = progressBar!!.max - progress
        }
        override fun onFinish() {
            finish()
        }
    }
}
