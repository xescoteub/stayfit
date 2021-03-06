package com.stayfit.ui.myroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.stayfit.R
import kotlinx.android.synthetic.main.routines_recycler_item.view.*
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection


class RoutineAdapterCalendar(var items: ArrayList<Routine>) : RecyclerView.Adapter<RoutineAdapterCalendar.RoutineViewHolder>() {
    class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        return RoutineViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.routine_recycler_calendar, parent,false ))
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
        var url = items.get(position).photo
        if (items.get(position).name.equals("Abs like rocks")){
            holder.routinePhoto.setImageResource(R.drawable.abs_default)
        }else if (items.get(position).name.equals("Run for life")){
            holder.routinePhoto.setImageResource(R.drawable.blog_2)
        }else {
            if (!url.equals("null")) {
                val currentUserID = FirebaseAuth.getInstance().currentUser?.uid.toString()
                var storageRef: StorageReference =
                    FirebaseStorage.getInstance().getReference().child("Backgrounds")
                        .child(currentUserID).child(url)
                storageRef.getBytes(1073741824).addOnSuccessListener {
                    holder.routinePhoto.setImageBitmap(
                        BitmapFactory.decodeByteArray(
                            it,
                            0,
                            it.size
                        )
                    )
                }
            } else {
                holder.routinePhoto.setImageResource(R.drawable.blog_5)
            }
        }

    }
    /**
     * Get bitmap from URL
     */
    open fun getImageBitmap(url: String): Bitmap? {
        var bm: Bitmap? = null
        try {
            val aURL = URL(url)
            val conn = aURL.openConnection()
            conn.connect()
            val `is` = conn.getInputStream()
            val bis = BufferedInputStream(`is`)
            bm = BitmapFactory.decodeStream(bis)
            bis.close()
            `is`.close()
        } catch (e: IOException) {
            Log.e("Routine adapter", "Error getting bitmap", e)
        }
        return bm
    }
}