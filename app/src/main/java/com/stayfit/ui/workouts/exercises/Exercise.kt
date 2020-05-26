package com.stayfit.ui.workouts.exercises


data class Exercise(var nom_exercise: String? = null, var url_video: String? = null, var time_count: String? = null, var jason: String? = null, var description: String? = null, var reps: String? = null) {
    fun getExerciseName(): String{return nom_exercise!!}
    fun getParametersList(): ArrayList<String>{
        var list: ArrayList<String> = ArrayList()
        list.add(nom_exercise!!)
        list.add(url_video!!)
        list.add(time_count!!)
        list.add(jason!!)
        list.add(description!!)
        if (reps==null){reps = "null"}
        list.add(reps!!)
        return list
    }
}
/*
data class Exercise(var nom_exercise: String, var url_video: String, var time_count: String, var jason: String, var description: String) {

}
*/