package com.stayfit.ui.home.blogs

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.stayfit.R
import kotlinx.android.synthetic.main.activity_blog_details.*
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

class BlogDetailsActivity : AppCompatActivity() {

    private val TAG = "BlogDetailsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_details);

        val blogImage = intent.getStringExtra("blog_photo") as String
        val blogName = intent.getStringExtra("blog_name") as String

        Log.d(TAG, "blogImage: $blogImage, blogName: $blogName")
        iv_blog_image.setImageBitmap(getImageBitmap(blogImage))
        tv_blog_name.text = intent.getStringExtra("blog_name")
        tv_blog_description.text = intent.getStringExtra("blog_description")

    }

    /**
     * Get bitmap from URL
     */
    private fun getImageBitmap(url: String): Bitmap?
    {
        var bm: Bitmap? = null
        try {
            val aURL = URL(url)
            val conn: URLConnection = aURL.openConnection()
            conn.connect()
            val `is`: InputStream = conn.getInputStream()
            val bis = BufferedInputStream(`is`)
            bm = BitmapFactory.decodeStream(bis)
            bis.close()
            `is`.close()
        } catch (e: IOException) {
            Log.e("BlogAdapter", "Error getting bitmap", e)
        }
        return bm
    }
}