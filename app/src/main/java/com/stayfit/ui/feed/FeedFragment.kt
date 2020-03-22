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
        carlist.add(Cars("Blog 1","Blog 1 description", R.drawable.blog_1) )
        carlist.add(Cars("Blog 2","Blog 2 description", R.drawable.blog_2) )
        carlist.add(Cars("Blog 3","Blog 2 description", R.drawable.blog_2) )
        carlist.add(Cars("Blog 4","Blog 3 description", R.drawable.blog_2) )
        carlist.add(Cars("Blog 5","Blog 4 description", R.drawable.blog_2) )
        carlist.add(Cars("Blog 6","Blog 5 description", R.drawable.blog_2) )
    }

    private fun showList() {
        carRecycler.layoutManager = LinearLayoutManager(activity)
        carRecycler.addItemDecoration(DividerItemDecoration(activity, 1))
        carRecycler.adapter = CarAdapter(carlist)
    }
}