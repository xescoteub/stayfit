package com.stayfit.ui.workouts.exercises

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.createDeviceProtectedStorageContext
import androidx.core.content.ContextCompat.startActivity
import com.stayfit.R
import kotlin.coroutines.coroutineContext

class Exercise(var nom_exercise: String, var url_video: String, var time_count: String, var jason: String, var description: String){
    fun getExerciseName(): String{return nom_exercise}
    fun getParametersList(): ArrayList<String>{
        var list: ArrayList<String> = ArrayList()
        list.add(nom_exercise)
        list.add(url_video)
        list.add(time_count)
        list.add(jason)
        list.add(description)
        return list
    }
}