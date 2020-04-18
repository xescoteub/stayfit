package com.stayfit.ui.myroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.stayfit.R
import kotlinx.android.synthetic.main.routines_recycler_item.view.*
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection


class RoutineAdapter(var items: ArrayList<Routine>) : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {
    class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        var routineName  = itemView.carname
        var routineDesc  = itemView.cardescription
        var routinePhoto = itemView.carlogo
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        return RoutineViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.routines_recycler_item, parent,false ))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        // It could be nullable, make it null safe adding ?
        holder.routineName?.text = items.get(position).name
        holder.routineDesc?.text = items.get(position).description
        //holder.routinePhoto.setImageResource(items.get(position).photo)
       // holder.routinePhoto.setImageBitmap(items.get(position).photo)
        if (items.get(position).photo!=null) {
            //holder.routinePhoto.setImageBitmap(items.get(position).photo)
            holder.routinePhoto.setImageResource(R.drawable.blog_1)
        }else{
            holder.routinePhoto.setImageResource(R.drawable.blog_5)
        }

    }
}