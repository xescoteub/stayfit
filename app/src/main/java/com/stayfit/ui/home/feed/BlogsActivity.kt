package com.stayfit.ui.home.feed

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.stayfit.R
import com.stayfit.config.BaseHTTPAction
import kotlinx.android.synthetic.main.activity_feed.*
import okhttp3.HttpUrl
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FeedActivity : BaseHTTPAction() {

    private val TAG = "FeedActivity"

    lateinit var blogList: ArrayList<Blog>

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    val blogsRef: DatabaseReference = database.getReference("blogs")

    private val FIREBASE_CLOUD_FUNCTION_USER_BLOGS_URL = "$baseURL/getUserBlogs"

    /**
     *
     */
    override fun responseRunnable(responseStr: String?): Runnable? {
        return Runnable {
            println("User blogs response: $responseStr")
        }
    }

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feed);

        // Toolbar config
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.app_name)

        toolbar.setNavigationOnClickListener { v: View? -> onBackPressed() }

        // Create list to hold blogs
        blogList = ArrayList()

        // Fetch blogs list
        fetchBlogs()

        getUserBlogs
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
                    val blogObj = it.getValue() as HashMap<*, *>
                    val blog = Blog()

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

    private val getUserBlogs: Unit
        get() {
            Log.d(TAG, "getUserBlogs")
            val httpBuilder = HttpUrl.parse(FIREBASE_CLOUD_FUNCTION_USER_BLOGS_URL)!!.newBuilder()
            sendMessageToCloudFunction(httpBuilder)
        }

}
