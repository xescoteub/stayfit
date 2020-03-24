package com.stayfit.ui.workouts.menu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.stayfit.R
import com.stayfit.ui.workouts.Category
import com.stayfit.ui.workouts.CategoryAdapter
import com.stayfit.ui.workouts.WorkoutsViewModel
import com.stayfit.ui.workouts.listexercises.AbsExercises
import com.stayfit.ui.workouts.listexercises.BackExercises
import com.stayfit.ui.workouts.listexercises.ChestExercises
import com.stayfit.ui.workouts.listexercises.LegExercises
import kotlinx.android.synthetic.main.workouts_fragment.*


class WorkoutsFragment : Fragment() {
    lateinit var categoriesList: ArrayList<Category>
    companion object {
        fun newInstance() = WorkoutsFragment()
    }

    private lateinit var viewModel: WorkoutsViewModel
    private val TAG = "WorkoutsFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: WorkoutsFragment created...")
        return inflater.inflate(R.layout.workouts_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WorkoutsViewModel::class.java)
        // TODO: Use the ViewModel
        categoriesList = ArrayList()
        addCategories()
        showList()
    }
    private fun addCategories() {
        categoriesList.add(Category("ARM","", R.drawable.arm) )
        categoriesList.add(Category("LEG","", R.drawable.leg) )
        categoriesList.add(Category("ABS","", R.drawable.abs) )
        categoriesList.add(Category("CHEST","", R.drawable.chest) )
        categoriesList.add(Category("BACK","", R.drawable.back) )
        categoriesList.add(Category("EXTRA","CARDIO\n" + "STRETCHES\n" + "YOGA\n" + "MEDITATION\n", R.drawable.extra) )
        categoriesList.add(Category("MY EXERCISES","", R.drawable.blog_5) )
    }

    private fun showList() {
        workoutRecycler.layoutManager = LinearLayoutManager(activity)
        workoutRecycler.addItemDecoration(DividerItemDecoration(activity, 0))
        workoutRecycler.adapter = CategoryAdapter(categoriesList)
        (workoutRecycler.adapter as CategoryAdapter).setOnItemClickListener(object :
            CategoryAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                startConcreteExercise(categoriesList[position].name)
            }
            override fun onItemLongClick(position: Int, v: View?) {
            }
        })
    }

    fun startConcreteExercise(s: String){
        if (s.equals("ARM")){startArmMenu()}
        else if (s.equals("LEG")){startLegExercises()}
        else if (s.equals("ABS")){startAbsExercises()}
        else if (s.equals("CHEST")){startChestExercises()}
        else if (s.equals("BACK")){startBackExercises()}
        else if (s.equals("EXTRA")){startExtraMenu()}
        else{ Toast.makeText(activity, "Exercise $s will be available coming soon!", Toast.LENGTH_SHORT).show()}
    }
    fun startArmMenu() {
        val intent = Intent(activity, ArmMenu::class.java)
        startActivity(intent)
    }
    fun startLegExercises() {
        val intent = Intent(activity, LegExercises::class.java)
        startActivity(intent)
    }
    fun startAbsExercises() {
        val intent = Intent(activity, AbsExercises::class.java)
        startActivity(intent)
    }
    fun startChestExercises() {
        val intent = Intent(activity, ChestExercises::class.java)
        startActivity(intent)
    }
    fun startBackExercises() {
        val intent = Intent(activity, BackExercises::class.java)
        startActivity(intent)
    }
    fun startExtraMenu() {
        val intent = Intent(activity, ExtraMenu::class.java)
        startActivity(intent)
    }
}
