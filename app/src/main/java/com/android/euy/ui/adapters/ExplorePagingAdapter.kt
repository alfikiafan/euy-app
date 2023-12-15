package com.android.euy.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.euy.data.model.Recipe
import com.android.euy.databinding.ItemFood2LayoutBinding
import com.bumptech.glide.Glide

class ExplorePagingAdapter(private val listener: (View, Recipe) -> Unit): PagingDataAdapter<Recipe, ExplorePagingAdapter.ViewHolder>(differCallback) {
    inner class ViewHolder(val binding: ItemFood2LayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ExplorePagingAdapter.ViewHolder, position: Int) {
        val recipe = getItem(position)
        with(holder) {
            Log.e("EXPLORE+ADAPTER",recipe.toString())
            binding.tvNamaFood.text = recipe!!.name
            Glide.with(holder.itemView.context)
                .load(recipe.image)
                .into(binding.imgFood)
            binding.tvTotalBahan.text = recipe.ingredients.size.toString() + " Bahan"
            binding.root.setOnClickListener {
                listener(it,recipe)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExplorePagingAdapter.ViewHolder {
        val binding = ItemFood2LayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}