package com.stayfit.ui.workouts.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.stayfit.R
import com.stayfit.ui.workouts.listexercises.BicepsExercises
import com.stayfit.ui.workouts.listexercises.ShoulderExercises
import com.stayfit.ui.workouts.listexercises.TricepsExercises
import kotlinx.android.synthetic.main.activity_arm_menu.*

class ArmMenu : AppCompatActivity() {
    lateinit var armList: ArrayList<ArmCategory>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arm_menu)
        armList = ArrayList()
        addCategories()
        showList()
    }
    private fun addCategories() {
        armList.add(ArmCategory("BICEPS","", R.drawable.biceps) )
        armList.add(ArmCategory("TRICEPS","", R.drawable.triceps) )
        armList.add(ArmCategory("SHOULDER","", R.drawable.shoulder) )
    }
    private fun showList() {
        armRecycler.layoutManager = LinearLayoutManager(this)
        armRecycler.addItemDecoration(DividerItemDecoration(this, 1))
        armRecycler.adapter = ArmCategoryAdapter(armList)
        (armRecycler.adapter as ArmCategoryAdapter).setOnItemClickListener(object :
            ArmCategoryAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                if (position<armList.size){startConcreteExercise(armList[position].name)}  //position mas grande que la mida -> solucionar
            }
            override fun onItemLongClick(position: Int, v: View?) {
            }
        })
    }
    fun startConcreteExercise(s: String){
        if (s.equals("BICEPS")){startBicepsExercises()}
        else if (s.equals("TRICEPS")){startTricepsExercises()}
        else{startShoulderExercises()}
    }
    fun startBicepsExercises() {
        val intent = Intent(this, BicepsExercises::class.java)
        startActivity(intent)
    }
    fun startTricepsExercises() {
        val intent = Intent(this, TricepsExercises::class.java)
        startActivity(intent)
    }
    fun startShoulderExercises() {
        val intent = Intent(this, ShoulderExercises::class.java)
        startActivity(intent)
    }
}
