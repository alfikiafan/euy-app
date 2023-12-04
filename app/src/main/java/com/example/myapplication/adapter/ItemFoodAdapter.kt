package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DataDummy.DataFood
import com.example.myapplication.DataDummy.DataRecom
import com.example.myapplication.R

class ItemFoodAdapter(private val listItemFood: ArrayList<DataFood>) : RecyclerView.Adapter<ItemFoodAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_makanan_recipe, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, photo, imgClock, Desc) = listItemFood[position]
        holder.imgPhoto.setImageResource(photo)
        holder.tvName.text = name
        holder.imgClock.setImageResource(imgClock)
        holder.tvReqDesc.text = Desc
    }

    override fun getItemCount(): Int {
        return listItemFood.size
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvReqDesc : TextView = itemView.findViewById(R.id.tv_item_description)
        val imgClock : ImageView = itemView.findViewById(R.id.img_clock)
    }
}