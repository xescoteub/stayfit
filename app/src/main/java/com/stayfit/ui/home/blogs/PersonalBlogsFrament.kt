package com.stayfit.ui.home.blogs

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
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


class PersonalBlogsFragment : Fragment() {
    private val TAG = "PersonalBlogsFragment"

    lateinit var blogList: ArrayList<Blog>

    private lateinit var mAuth: FirebaseAuth

    private val db = FirebaseFirestore.getInstance()

    var baseURL = "https://us-central1-stayfit-87c1a.cloudfunctions.net"

    private val FIREBASE_CLOUD_FUNCTION_BASE_URL = "$baseURL/api"

    private var addButton: FloatingActionButton? = null

    private var progressCircular: ProgressBar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.personal_blogs_fragment, container, false)

        // Instantiate firebase auth
        mAuth = FirebaseAuth.getInstance()

        // Create list to hold blogs
        blogList = ArrayList()

        progressCircular = root.findViewById<View>(R.id.progress_circular) as ProgressBar

        addButton = root.findViewById<View>(R.id.btn_add_blog) as FloatingActionButton
        addButton!!.setOnClickListener {
            Log.d(TAG, "addButton clicked!")
            showNewNameDialog()
        }

        // Fetch user blogs
        fetchUserBlogs()

        return root
    }

    /**
     *
     */
    private fun showNewNameDialog() {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.create_blog_dialog, null)
        dialogBuilder.setView(dialogView)

        val blogName = dialogView.findViewById<EditText>(R.id.et_blog_name)
        val blogDescription = dialogView.findViewById<EditText>(R.id.et_blog_desc)

        dialogBuilder.setTitle("Custom new blog")
        dialogBuilder.setPositiveButton("Save") { dialog, whichButton ->
            //do something with edt.getText().toString();
            val payload = hashMapOf(
                "user_id" to mAuth.uid,
                "name" to blogName.text.toString(),
                "description" to blogDescription.text.toString()
            )
            db.collection("blogs")
                .add(payload)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
            // fetch user blogs after blog has been inserted into database
            fetchUserBlogs()
        }

        dialogBuilder.setNegativeButton("Cancel") { _, _ ->
            //pass
        }
        val b = dialogBuilder.create()
        b.show()
    }

    /**
     *
     */
    private fun fetchUserBlogs()
    {
        Log.d(TAG, "getUserBlogs")
        progressCircular?.visibility = View.VISIBLE;

        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("$FIREBASE_CLOUD_FUNCTION_BASE_URL/blogs/user/${mAuth.uid}")
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(request: Request?, e: IOException?) {

            }

            override fun onResponse(response: Response?) {
                val myResponse: String = response?.body()!!.string()
                this@PersonalBlogsFragment.activity?.runOnUiThread(Runnable {
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
                            blogList.add(blog)

                            showList()
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
        blogRecycler.layoutManager = LinearLayoutManager(activity)
        blogRecycler.addItemDecoration(DividerItemDecoration(activity, 1))
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

    fun preventClicks(view: View?) {}
}