package com.stayfit.ui.myroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stayfit.R
import kotlinx.android.synthetic.main.routines_recycler_item.view.*
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

class DefaultRoutineAdapter(var items: ArrayList<Routine>) : RecyclerView.Adapter<DefaultRoutineAdapter.DefaultRoutineViewHolder>() {
    class DefaultRoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        var routineName  = itemView.blogName
        var routineDesc  = itemView.blogDescription
        var routinePhoto = itemView.blogImage
        override fun onClick(v: View?) {
            clickListener?.onItemClick(adapterPosition, v)
        }

        override fun onLongClick(v: View?): Boolean {
            clickListener?.onItemLongClick(adapterPosition, v)
            return false
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }
    }

    fun setOnItemClickListener(clickListener: ClickListener?) {
        Companion.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int, v: View?)
        fun onItemLongClick(position: Int, v: View?)
    }

    companion object {
        private var clickListener: ClickListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultRoutineViewHolder {
        return DefaultRoutineViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.routines_recycler_item, parent,false ))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: DefaultRoutineViewHolder, position: Int) {
        // It could be nullable, make it null safe adding ?
        holder.routineName?.text = items.get(position).name
        holder.routineDesc?.text = items.get(position).description
        holder.routinePhoto.setImageResource(items.get(position).photo.toInt())

    }
}