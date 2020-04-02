package com.stayfit.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.stayfit.R
import kotlinx.android.synthetic.main.feed_fragment.*


class FeedFragment : Fragment() {
    lateinit var blogList: ArrayList<Blog>
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val blogsRef: DatabaseReference = database.getReference("blogs")

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

        println("blogsRef: $blogsRef")

        blogList = ArrayList()
        addBlogs()
        showList()
    }

    private fun addBlogs() {
        /*blogList.add(Blog("Blog 1","Blog 1 description", R.drawable.blog_1) )
        blogList.add(Blog("Blog 2","Blog 2 description", R.drawable.blog_2) )
        blogList.add(Blog("Blog 3","Blog 2 description", R.drawable.blog_3) )
        blogList.add(Blog("Blog 4","Blog 3 description", R.drawable.blog_4) )
        blogList.add(Blog("Blog 5","Blog 4 description", R.drawable.blog_5) )*/

        // Read from the database
        blogsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    var itemVal: Any? = item.getValue();
                    Log.d(TAG, "Item is: $item")
                    Log.d(TAG, "Item val is: ${itemVal}")
                }


                /*val children = dataSnapshot.children
                children.forEach {
                    println(it.toString())
                    blogList.add(Blog("Blog 100","Blog 100 description", R.drawable.blog_1))
                }*/
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //val value = dataSnapshot.value as Map<*, *>?

                //Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }


    private fun showList() {
        blogRecycler.layoutManager = LinearLayoutManager(activity)
        blogRecycler.addItemDecoration(DividerItemDecoration(activity, 1))
        blogRecycler.adapter = BlogAdapter(blogList)
    }
}