package com.stayfit.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stayfit.R
import kotlinx.android.synthetic.main.feed_recycler_item.view.*

/**
 * Connect activity with it's data
 */
class BlogAdapter(var items: ArrayList<Blog>) : RecyclerView.Adapter<BlogViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        return BlogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.feed_recycler_item, parent,false ))
    }

    /**
     *
     */
    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        // It could be nullable, make it null safe adding ?
        holder.blogName?.text = items.get(position).name
        holder.blogDesc?.text = items.get(position).description
        holder.blogPhoto.setImageResource(items.get(position).photo)
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
class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var blogName  = itemView.carname
    var blogDesc  = itemView.cardescription
    var blogPhoto = itemView.carlogo

    /*fun initialize(item: Blog, action:OnBlogItemClickListner) {
        blogName.text = item.name
        blogDesc.text = item.description
        blogPhoto.setImageResource(item.photo)

        itemView.setOnClickListener{
            action.onItemClick(item, adapterPosition)
        }
    }*/
}