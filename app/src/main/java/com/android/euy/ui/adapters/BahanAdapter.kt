package com.android.euy.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.euy.databinding.ItemBahanPilihanLayoutBinding

class BahanAdapter(private var mListBahan: List<String>): RecyclerView.Adapter<BahanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BahanAdapter.ViewHolder {
        val binding = ItemBahanPilihanLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: BahanAdapter.ViewHolder, position: Int) {

    }

    inner class ViewHolder(val binding: ItemBahanPilihanLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}