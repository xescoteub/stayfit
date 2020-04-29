package com.stayfit.ui.workouts.exercises


open class Exercise(var nom_exercise: String, var url_video: String, var time_count: String, var jason: String, var description: String) {
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
/*
* open class Exercise(var nom_exercise: String, var url_video: String, var time_count: String, var jason: String, var description: String):Parcelable{
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
    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Exercise> {
            override fun createFromParcel(parcel: Parcel) = Exercise(parcel)
            override fun newArray(size: Int) = arrayOfNulls<Exercise>(size)
        }
    }


    private constructor(parcel: Parcel) : this(
        nom_exercise = parcel.readString()!!,
        url_video = parcel.readString()!!,
        time_count = parcel.readString()!!,
        jason = parcel.readString()!!,
        description = parcel.readString()!!
    ){
        val data = arrayOfNulls<String>(3)
        parcel.readStringArray(data)
        this.nom_exercise = data[0]!!
        this.url_video = data[1]!!
        this.time_count = data[2]!!
        this.jason = data[3]!!
        this.description = data[4]!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nom_exercise)
        parcel.writeString(url_video)
        parcel.writeString(time_count)
        parcel.writeString(jason)
        parcel.writeString(description)
    }
    private fun readFromParcel(parcel: Parcel){
        nom_exercise = parcel.readString()!!
        url_video = parcel.readString()!!
        time_count = parcel.readString()!!
        jason = parcel.readString()!!
        description = parcel.readString()!!
    }

    override fun describeContents() = 0
}*/