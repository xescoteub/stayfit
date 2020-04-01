package com.stayfit.ui.workouts.exercises

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.stayfit.R

class ExerciseActivity(var nom_exercise: String) : AppCompatActivity() {
    var progressBar: ProgressBar? = null
    var myCountDownTimer: MyCountDownTimer? = null
    var text_time: TextView? = null
    private var delay:Delay = Delay(3000, 1000)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        progressBar = findViewById<ProgressBar>(R.id.progressBar_exercise)
        text_time = findViewById<TextView>(R.id.txt_time)
        progressBar!!.getProgressDrawable().setColorFilter(Color.parseColor("#233475"), android.graphics.PorterDuff.Mode.SRC_IN);
        downProgressBar(15000) //10s
    }
    private fun downProgressBar(durada: Long){
        myCountDownTimer = MyCountDownTimer(durada, 1000)
        progressBar!!.max=(durada / 1000).toInt()
        progressBar!!.progress=progressBar!!.max
        text_time!!.setText(progressBar!!.progress.toString()+'"'+"/"+progressBar!!.max.toString()+'"')
        delay.start()
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
    inner class Delay(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
        }
        override fun onFinish() {
        }
    }

    fun openinYT(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"))
        startActivity(intent)
    }
    public fun getExerciseName(): String{return nom_exercise}
}
