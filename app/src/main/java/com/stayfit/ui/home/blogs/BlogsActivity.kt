package com.stayfit.ui.home.blogs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.squareup.okhttp.*
import com.stayfit.R
import kotlinx.android.synthetic.main.activity_blogs.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class BlogsActivity : AppCompatActivity() {

    private val TAG = "BlogsActivity"

    lateinit var blogList: ArrayList<Blog>

    private lateinit var mAuth: FirebaseAuth

    var baseURL = "https://us-central1-stayfit-87c1a.cloudfunctions.net"

    private val FIREBASE_CLOUD_FUNCTION_BASE_URL = "$baseURL/api"

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_blogs);

        // Toolbar config
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.app_name)

        toolbar.setNavigationOnClickListener { v: View? -> onBackPressed() }

        // Create list to hold blogs
        blogList = ArrayList()

        getUserBlogs()
    }

    /**
     *
     */
    private fun getUserBlogs()
    {
        Log.d(TAG, "getUserBlogs")

        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("$FIREBASE_CLOUD_FUNCTION_BASE_URL/blogs/user/${mAuth.uid}")
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(request: Request?, e: IOException?) {

            }

            override fun onResponse(response: Response?) {
                val myResponse: String = response?.body()!!.string()
                this@BlogsActivity.runOnUiThread(Runnable {
                    try {
                        val json = JSONArray(myResponse)

                        // Generate a new blog object for each received document
                        for (i in 0 until json.length()) {
                            val item = json.getJSONObject(i)
                            val blog = Blog()
                            with(blog) {
                                name            = item["name"].toString()
                                description     = item["description"].toString()
                                photo           = item["image"].toString()
                            }
                            Log.d(TAG, "> blog : $blog")
                            blogList.add(blog)

                            showList();
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                })
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