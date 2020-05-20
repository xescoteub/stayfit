package com.stayfit.ui.myroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.stayfit.R
import kotlinx.android.synthetic.main.routines_recycler_item.view.*
import java.io.BufferedInputStream
import java.io.IOException
import java.net.URL


class RoutineAdapter(var items: ArrayList<Routine>) : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {
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
        // Create a reference with an initial file path and name
        var url = items.get(position).photo

        if (!url.equals("null")) {
            val currentUserID = FirebaseAuth.getInstance().currentUser?.uid.toString()
            var storageRef: StorageReference = FirebaseStorage.getInstance().getReference().child("Backgrounds").child(currentUserID).child(url)
            storageRef.getBytes(1073741824).addOnSuccessListener {
                holder.routinePhoto.setImageBitmap(BitmapFactory.decodeByteArray(it,0,it.size))
            }
            /*
            Log.e("RoutineAdapter", "$url")
            storageRef.child("1589965060105.jpg").downloadUrl
                .addOnSuccessListener {
                    // val image = BitmapFactory.decodeStream(URL(it!!.toString()) .openConnection().getInputStream())
                    //holder.routinePhoto.setImageBitmap(image)
                    //holder.routinePhoto.setImageURI(it!!)
                    url = it!!.toString()
                    Log.e("RoutineAdapter", "$url")
                    //holder.routinePhoto.setImageResource(R.drawable.blog_1)
                    holder.routinePhoto.setImageBitmap(getImageBitmap(url))
                }.addOnFailureListener {
                    // Handle any errors
                    holder.routinePhoto.setImageResource(R.drawable.blog_1)
                }

             */
        }else{
            holder.routinePhoto.setImageResource(R.drawable.blog_5)
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