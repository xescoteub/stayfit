package com.stayfit.ui.feed

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.stayfit.R
import kotlinx.android.synthetic.main.feed_fragment.*

class FeedFragment : Fragment() {
    lateinit var carlist: ArrayList<Cars>

    companion object {
        fun newInstance() = FeedFragment()
    }

    private lateinit var viewModel: FeedViewModel

    private val TAG = "FeedFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: FeedFragment created...")
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        carlist = ArrayList()
        addCars()
        showList()
    }

    private fun addCars() {
        carlist.add(Cars("Toyota","good car, good car, good car, good car, good car, good car, good car, good car, good car, good car", R.drawable.toyota) )
        carlist.add(Cars("Hyundai","good car, good car, good car, good car, good car, good car, good car, good car, good car, good car", R.drawable.hyundai) )
        carlist.add(Cars("Marcedese","good car, good car, good car, good car, good car, good car, good car, good car, good car, good car", R.drawable.marcedese) )
        carlist.add(Cars("Bentley","good car, good car, good car, good car, good car, good car, good car, good car, good car, good car", R.drawable.bentley) )
        carlist.add(Cars("Nissan","good car, good car, good car, good car, good car, good car, good car, good car, good car, good car", R.drawable.nissan) )
        carlist.add(Cars("Ford","good car, good car, good car, good car, good car, good car, good car, good car, good car, good car", R.drawable.ford) )
    }

    private fun showList() {
        carRecycler.layoutManager = LinearLayoutManager(activity)
        carRecycler.addItemDecoration(DividerItemDecoration(activity, 1))
        carRecycler.adapter = CarAdapter(carlist)
    }
}