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
class CarAdapter(var items: ArrayList<Cars>) : RecyclerView.Adapter<CarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        lateinit var carViewHolder: CarViewHolder
        carViewHolder = CarViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.feed_recycler_item, parent,false ))

        return carViewHolder
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        // It could be nullable, make it null safe adding ?
        holder.carName?.text = items.get(position).name
        holder.carDescription?.text = items.get(position).description
        holder.carLogo.setImageResource(items.get(position).logo)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var carName = itemView.carname
    var carDescription = itemView.cardescription
    var carLogo = itemView.carlogo

    /*fun initialize(item: Cars, action:OnCarItemClickListner) {
        carName.text = item.name
        carDescription.text = item.description
        carLogo.setImageResource(item.logo)

        itemView.setOnClickListener{
            action.onItemClick(item,adapterPosition)
        }
    }*/
}