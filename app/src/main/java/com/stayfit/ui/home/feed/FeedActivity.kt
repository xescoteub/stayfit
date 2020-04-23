package com.stayfit.ui.home.feed

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.stayfit.R
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {

    private val TAG = "FeedActivity"

    lateinit var blogList: ArrayList<Blog>

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    val blogsRef: DatabaseReference = database.getReference("blogs")

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feed);

        println("blogsRef: $blogsRef")

        blogList = ArrayList()
        fetchBlogs()
    }

    /**
     * Get blogs list from database
     */
    private fun fetchBlogs()
    {
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
                        photo           = blogObj["banner_image"].toString()
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
        blogRecycler.layoutManager = LinearLayoutManager(this)
        blogRecycler.addItemDecoration(DividerItemDecoration(this, 1))
        blogRecycler.adapter = BlogAdapter(blogList)
    }
}
