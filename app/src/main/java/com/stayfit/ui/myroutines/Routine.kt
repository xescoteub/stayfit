package com.stayfit.ui.myroutines

import android.graphics.Bitmap
import com.stayfit.ui.workouts.exercises.Exercise

data class Routine(var name: String, var description: String, var photo: Bitmap?, var arrayExercises: ArrayList<Exercise>){
    fun getParametersList(): ArrayList<String>{
        var list: ArrayList<String> = ArrayList()
        list.add(name)
        list.add(arrayExercises.toString())
        return list
    }
    fun getExercisesList(): ArrayList<Exercise>{
        return arrayExercises
    }
    fun addExercise(ex: Exercise){arrayExercises.add(ex)}
}