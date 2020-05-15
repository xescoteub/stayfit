package com.stayfit.ui.activity

import androidx.lifecycle.ViewModel

class ActivityViewModel : ViewModel() {
    var namesRoutines: ArrayList<String> = ArrayList()
    fun setRoutines(list: ArrayList<String>){
        namesRoutines = list
    }
}