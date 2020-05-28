package com.stayfit.ui.home.blogs

import BlogViewPagerAdapter
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import com.stayfit.R
import kotlinx.android.synthetic.main.personal_blogs_fragment.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class BlogsActivity : AppCompatActivity() {

    private val TAG = "BlogsActivity"

    lateinit var blogList: ArrayList<Blog>

    private lateinit var mAuth: FirebaseAuth

    private val db = FirebaseFirestore.getInstance()

    var baseURL = "https://us-central1-stayfit-87c1a.cloudfunctions.net"

    private val FIREBASE_CLOUD_FUNCTION_BASE_URL = "$baseURL/api"

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

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

        // Tabs config
        tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager = findViewById<ViewPager>(R.id.blogsViewPager)

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Personal blogs"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Recommended blogs"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = BlogViewPagerAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })


        // Create list to hold blogs
        blogList = ArrayList()

        getUserBlogs()
    }

    fun preventClicks(view: View?) {}


    /**
     *
     */
    private fun getUserBlogs()
    {
        Log.d(TAG, "getUserBlogs")
        //progress_circular.visibility = View.VISIBLE;

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
                        println("json: $json")
                        // Generate a new blog object for each received document
                        for (i in 0 until json.length()) {
                            val item = json.getJSONObject(i)
                            /*val blog = Blog()
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

    /**
     * Display list of blogs and attach click listener to list items.
     * When item is clicked, the blog details activity is started.
     */
    private fun showList() {
        blogRecycler.layoutManager = LinearLayoutManager(this)
        blogRecycler.addItemDecoration(DividerItemDecoration(this, 1))
        blogRecycler.adapter = BlogAdapter(blogList)
        (blogRecycler.adapter as BlogAdapter).setOnItemClickListener(object :
            BlogAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                startBlogDetailsActivity(blogList[position])
            }

            override fun onItemLongClick(position: Int, v: View?) {
                TODO("Not yet implemented")
            }
        })
    }

    /**
     *
     */
    private fun startBlogDetailsActivity(item: Blog) {
        Log.d(TAG,"startBlogDetailsActivity >>>  $item")
        val intent = Intent(this, BlogDetailsActivity::class.java)
        intent.putExtra("blog_name", item.name)
        intent.putExtra("blog_description", item.description)
        intent.putExtra("blog_photo", item.photo)
        startActivity(intent)
    }
}