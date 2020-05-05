package com.stayfit.ui.home.blogs

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stayfit.R
import kotlinx.android.synthetic.main.blog_recycler_item.view.*
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

/**
 * Connect activity with it's data
 */
class BlogAdapter(var items: ArrayList<Blog>) : RecyclerView.Adapter<BlogViewHolder>() {

    /**
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder
    {
        return BlogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.blog_recycler_item, parent,false ))
    }

    /**
     *
     */
    override fun onBindViewHolder(holder: BlogViewHolder, position: Int)
    {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // It could be nullable, make it null safe adding ?
        holder.blogName?.text = items.get(position).name
        holder.blogDesc?.text = items.get(position).description
        //holder.blogPhoto.setImageResource(items.get(position).photo)
        val url = items.get(position).photo;

        holder.blogPhoto.setImageBitmap(getImageBitmap(url));
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

    /**
     *
     */
    override fun getItemCount(): Int {
        return items.size
    }
}

/**
 *
 */
class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    var blogName  = itemView.blogName
    var blogDesc  = itemView.blogDescription
    var blogPhoto = itemView.blogImage

    /*fun initialize(item: Blog, action:OnBlogItemClickListner) {
        blogName.text = item.name
        blogDesc.text = item.description
        blogPhoto.setImageResource(item.photo)

        itemView.setOnClickListener{
            action.onItemClick(item, adapterPosition)
        }
    }*/
}