package com.stayfit.ui.home.blogs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import com.stayfit.R
import kotlinx.android.synthetic.main.recommended_blogs_fragment.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class RecommendedBlogsFrament : Fragment() {
    private val TAG = "RecommendedBlogsFrament"

    lateinit var blogsList: ArrayList<Blog>

    private lateinit var mAuth: FirebaseAuth

    var baseURL = "https://us-central1-stayfit-87c1a.cloudfunctions.net"

    private val FIREBASE_CLOUD_FUNCTION_BASE_URL = "$baseURL/api"

    private var progressCircular: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.recommended_blogs_fragment, container, false)

        mAuth = FirebaseAuth.getInstance()

        progressCircular = root.findViewById<View>(R.id.recommended_blogs_progress) as ProgressBar

        // Create list to hold blogs
        blogsList = ArrayList()
        
        // Fetch user recommended blogs
        fetchUserRecommendedBlogs()

        return root
    }

    /**
     *
     */
    private fun fetchUserRecommendedBlogs()
    {
        Log.d(TAG, "fetchUserRecommendedBlogs")
        progressCircular?.visibility = View.VISIBLE;

        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("$FIREBASE_CLOUD_FUNCTION_BASE_URL/blogs/recommended/user/${mAuth.uid}")
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(request: Request?, e: IOException?) {

            }

            override fun onResponse(response: Response?) {
                val myResponse: String = response?.body()!!.string()
                this@RecommendedBlogsFrament.activity?.runOnUiThread(Runnable {
                    try {
                        val json = JSONArray(myResponse)
                        println("json: $json")
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
                            blogsList.add(blog)

                            showList();
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } finally {
                        progressCircular?.visibility = View.GONE;
                    }
                })
            }
        })
    }

    /**
     *
     */
    private fun startBlogDetailsActivity(item: Blog) {
        val intent = Intent (activity, BlogDetailsActivity::class.java)
        intent.putExtra("blog_name", item.name)
        intent.putExtra("blog_description", item.description)
        intent.putExtra("blog_photo", item.photo)
        startActivity(intent)
    }

    /**
     * Display list of blogs and attach click listener to list items.
     * When item is clicked, the blog details activity is started.
     */
    private fun showList() {
        recommendedBlogRecycler.layoutManager = LinearLayoutManager(activity)
        recommendedBlogRecycler.addItemDecoration(DividerItemDecoration(activity, 1))
        recommendedBlogRecycler.adapter = BlogAdapter(blogsList)
        (recommendedBlogRecycler.adapter as BlogAdapter).setOnItemClickListener(object :
            BlogAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                startBlogDetailsActivity(blogsList[position])
            }

            override fun onItemLongClick(position: Int, v: View?) {
                TODO("Not yet implemented")
            }
        })
    }
}