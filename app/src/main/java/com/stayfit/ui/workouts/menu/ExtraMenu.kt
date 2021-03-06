package com.stayfit.ui.workouts.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.stayfit.R
import com.stayfit.ui.workouts.listexercises.CardioExercises
import com.stayfit.ui.workouts.listexercises.MeditationExercises
import com.stayfit.ui.workouts.listexercises.StretchesExercises
import com.stayfit.ui.workouts.listexercises.YogaExercises
import kotlinx.android.synthetic.main.activity_extra_menu.*

class ExtraMenu : AppCompatActivity() {
    lateinit var extraList: ArrayList<ExtraCategory>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_menu)
        extraList = ArrayList()
        addCategories()
        showList()
    }
    private fun addCategories() {
        extraList.add(ExtraCategory("CARDIO","", R.drawable.cardio) )
        extraList.add(ExtraCategory("STRETCHES","", R.drawable.stretches) )
        extraList.add(ExtraCategory("YOGA","", R.drawable.yoga) )
        extraList.add(ExtraCategory("MEDITATION","", R.drawable.meditation) )
    }
    private fun showList() {
        extraRecycler.layoutManager = LinearLayoutManager(this)
        extraRecycler.addItemDecoration(DividerItemDecoration(this, 1))
        extraRecycler.adapter = ExtraCategoryAdapter(extraList)
        (extraRecycler.adapter as ExtraCategoryAdapter).setOnItemClickListener(object :
            ExtraCategoryAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                if (position<extraList.size){startConcreteExercise(extraList[position].name)} //position mas grande que la mida -> solucionar
            }
            override fun onItemLongClick(position: Int, v: View?) {
            }
        })
    }

    fun startConcreteExercise(s: String){
        if (s.equals("CARDIO")){startCardioExercises()}
        else if (s.equals("STRETCHES")){startStretchesExercises() }
        else if (s.equals("YOGA")){startYogaExercises()}
        else{ startMeditationExercises()}
    }
    fun startCardioExercises() {
        val intent = Intent(this, CardioExercises::class.java)
        startActivity(intent)
    }
    fun startStretchesExercises() {
        val intent = Intent(this, StretchesExercises::class.java)
        startActivity(intent)
    }
    fun startYogaExercises() {
        val intent = Intent(this, YogaExercises::class.java)
        startActivity(intent)
    }
    fun startMeditationExercises() {
        val intent = Intent(this, MeditationExercises::class.java)
        startActivity(intent)
    }
}
