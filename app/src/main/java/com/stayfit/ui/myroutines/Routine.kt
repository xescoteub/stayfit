package com.stayfit.ui.myroutines

import com.stayfit.ui.workouts.exercises.Exercise

data class Routine(var name: String = "", var description: String = "", var photo: String = "", var hashMapExercises: HashMap<String, ArrayList<ArrayList<String>>>? = null)

/*data class Routine(var name: String, var description: String, var photo: String, var hashMapExercises: HashMap<String, ArrayList<ArrayList<String>>>){
    fun getParametersList(): ArrayList<String>{
        var list: ArrayList<String> = ArrayList()
        list.add(name)
        list.add(hashMapExercises.toString())
        return list
    }
    fun getExercisesList(): HashMap<String, ArrayList<ArrayList<String>>>{
        return hashMapExercises
    }
    fun addExercise(ex: Exercise){
        var exercises:ArrayList<ArrayList<String>> ?= hashMapExercises["exercises"]
        exercises!!.add(ex.getParametersList())
        hashMapExercises["exercises"] = exercises
    }
    fun getNameRoutine(): String{
        return name
    }
}
*/