package com.stayfit.ui.myroutines

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson

class MyRoutinesViewModel : ViewModel(){
    var namesRoutines: ArrayList<String> = ArrayList()
    fun setRoutines(list: ArrayList<String>){
        namesRoutines = list
    }
    fun getRoutines(): ArrayList<String> {
        return namesRoutines
    }

}