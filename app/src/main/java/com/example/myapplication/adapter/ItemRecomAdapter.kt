package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DataDummy.DataRecom
import com.example.myapplication.R

class ItemRecomAdapter(private val listItemRecom: ArrayList<DataRecom>) : RecyclerView.Adapter<ItemRecomAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_recom, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, photo) = listItemRecom[position]
        holder.imgPhoto.setImageResource(photo)
        holder.tvName.text = name
    }

    override fun getItemCount(): Int {
        return listItemRecom.size
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.recom_image)
        val tvName: TextView = itemView.findViewById(R.id.recom_title)
    }
}
