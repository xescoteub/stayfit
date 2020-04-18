package com.stayfit.ui.workouts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stayfit.R
import kotlinx.android.synthetic.main.workouts_recycler_item.view.*


/**
 * Connect activity with it's data
 */
/*
class CategoryAdapter(var items: ArrayList<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.workouts_recycler_item, parent,false ))
    }

    /**
     *
     */
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        // It could be nullable, make it null safe adding ?
        holder.categoryName?.text = items.get(position).name
        holder.categoryDesc?.text = items.get(position).description
        holder.categoryPhoto.setImageResource(items.get(position).photo)
    }

    /**
     *
     */
    override fun getItemCount(): Int {
        return items.size
    }

}*/
class CategoryAdapter(var items: ArrayList<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        var categoryName  = itemView.blogName
        var categoryDesc  = itemView.blogDescription
        var categoryPhoto = itemView.blogImage
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.workouts_recycler_item, parent,false ))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        // It could be nullable, make it null safe adding ?
        holder.categoryName?.text = items.get(position).name
        holder.categoryDesc?.text = items.get(position).description
        holder.categoryPhoto.setImageResource(items.get(position).photo)
    }
}
/**
 *
 */
/*class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
    var categoryName  = itemView.blogName
    var categoryDesc  = itemView.blogDescription
    var categoryPhoto = itemView.blogImage
    fun ViewHolder(itemView: View) {
        super(itemView)
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
        name = itemView.findViewById<View>(R.id.card_name) as TextView
    }

    override fun onClick(p0: View?) {
    }

    override fun onLongClick(p0: View?): Boolean {
    }


    /*fun initialize(item: Category, action: AdapterView.OnItemClickListener) {
        categoryName.text = item.name
        categoryDesc.text = item.description
        categoryPhoto.setImageResource(item.photo)

        itemView.setOnClickListener{
            action.onItemClick(item, adapterPosition)
        }
    }*/
}*/