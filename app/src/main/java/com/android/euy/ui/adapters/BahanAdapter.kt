package com.android.euy.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.euy.databinding.ItemBahanPilihanLayoutBinding

class BahanAdapter(private var mListBahan: ArrayList<String>): RecyclerView.Adapter<BahanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BahanAdapter.ViewHolder {
        val binding = ItemBahanPilihanLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mListBahan.size
    }

    override fun onBindViewHolder(holder: BahanAdapter.ViewHolder, position: Int) {
        val text = mListBahan.get(position)
        holder.bind(text)
        holder.itemView.setOnClickListener{
            mListBahan.remove(text)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(val binding: ItemBahanPilihanLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(text: String){
            binding.textBahan.text = text
        }
    }
}