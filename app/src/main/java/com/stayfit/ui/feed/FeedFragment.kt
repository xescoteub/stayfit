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
    lateinit var blogList: ArrayList<Blog>

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
        blogList = ArrayList()
        addCars()
        showList()
    }

    private fun addCars() {
        blogList.add(Blog("Blog 1","Blog 1 description", R.drawable.blog_1) )
        blogList.add(Blog("Blog 2","Blog 2 description", R.drawable.blog_2) )
        blogList.add(Blog("Blog 3","Blog 2 description", R.drawable.blog_3) )
        blogList.add(Blog("Blog 4","Blog 3 description", R.drawable.blog_4) )
        blogList.add(Blog("Blog 5","Blog 4 description", R.drawable.blog_5) )
    }

    private fun showList() {
        blogRecycler.layoutManager = LinearLayoutManager(activity)
        blogRecycler.addItemDecoration(DividerItemDecoration(activity, 1))
        blogRecycler.adapter = BlogAdapter(blogList)
    }
}