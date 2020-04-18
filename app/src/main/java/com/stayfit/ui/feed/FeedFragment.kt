package com.stayfit.ui.feed

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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
    }

    /**
     * Get blogs list from database
     */
    private fun addBlogs()
    {
        blogList.add(Blog("Local blog 1","Local blog 1 description", R.drawable.blog_1) )

        // Read from the database
        blogsRef.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val map = dataSnapshot.getValue()
                Log.d(TAG, "map: ${map}")

                dataSnapshot.children.forEach {
                    var blogObj = it.getValue() as HashMap<*, *>
                    var blog = Blog()

                    with(blog) {
                        name            = blogObj["name"].toString()
                        description     = blogObj["description"].toString()
                        photo           = R.drawable.blog_1
                    }
                    Log.d(TAG, "> blog: ${blog}")
                    blogList.add(blog)

                    showList();
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    /**
     * Display list of blogs
     */
    private fun showList() {
        blogRecycler.layoutManager = LinearLayoutManager(activity)
        blogRecycler.addItemDecoration(DividerItemDecoration(activity, 1))
        blogRecycler.adapter = BlogAdapter(blogList)
    }
}
