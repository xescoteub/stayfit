package com.stayfit.ui.home.blogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import com.stayfit.R
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class RecommendedBlogsFrament : Fragment() {
    private val TAG = "RecommendedBlogsFrament"

    lateinit var blogList: ArrayList<Blog>

    private lateinit var mAuth: FirebaseAuth

    var baseURL = "https://us-central1-stayfit-87c1a.cloudfunctions.net"

    private val FIREBASE_CLOUD_FUNCTION_BASE_URL = "$baseURL/api"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.recommended_blogs_fragment, container, false)

        mAuth = FirebaseAuth.getInstance()

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
        //progress_circular.visibility = View.VISIBLE;

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
                            /*
                                val blog = Blog()
                                with(blog) {
                                    name            = item["name"].toString()
                                    description     = item["description"].toString()
                                    photo           = item["image"].toString()
                                }
                                Log.d(TAG, "> blog : $blog")
                                blogList.add(blog)

                                showList();
                            */
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } finally {
                        //progress_circular.visibility = View.GONE;
                    }
                })
            }
        })
    }
}