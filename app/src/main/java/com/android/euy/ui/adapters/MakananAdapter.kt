package com.android.euy.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.euy.data.model.Food
import com.android.euy.databinding.ItemFavoriteLayoutBinding
import com.android.euy.databinding.ItemFood2LayoutBinding
import com.android.euy.databinding.ItemFoodLayoutBinding
import com.android.euy.databinding.ItemResepLayoutBinding

class MakananAdapter(private val mContext: Context, private val TYPE: Int, private val mFoodList: List<Food>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    companion object {
        private const val VIEW_TYPE_LAYOUT_1 = 1
        private const val VIEW_TYPE_LAYOUT_2 = 2
        private const val VIEW_TYPE_LAYOUT_3 = 3
        private const val VIEW_TYPE_LAYOUT_4 = 4
    }

    override fun getItemViewType(position: Int): Int {
        return if (TYPE == 1) {
            VIEW_TYPE_LAYOUT_1
        } else if (TYPE == 2) {
            VIEW_TYPE_LAYOUT_2
        }else if (TYPE == 3) {
            VIEW_TYPE_LAYOUT_3
        }else {
            VIEW_TYPE_LAYOUT_4
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val binding = ItemProductDaurMarketBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        return ViewHolder(binding)
        return when (viewType) {
            VIEW_TYPE_LAYOUT_1 -> {
                val view = ItemFoodLayoutBinding.inflate(LayoutInflater.from(mContext),parent,false)
                UtamaHolder(view)
            }
            VIEW_TYPE_LAYOUT_2 -> {
                val view = ItemFood2LayoutBinding.inflate(LayoutInflater.from(mContext),parent,false)
                ExploreHolder(view)
            }
            VIEW_TYPE_LAYOUT_3 -> {
                val view = ItemFavoriteLayoutBinding.inflate(LayoutInflater.from(mContext),parent,false)
                FavoriteHolder(view)
            }
            VIEW_TYPE_LAYOUT_4 -> {
                val view = ItemResepLayoutBinding.inflate(LayoutInflater.from(mContext),parent,false)
                ResepHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return mFoodList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val food = mFoodList.get(position)
        when (holder.itemViewType) {
            VIEW_TYPE_LAYOUT_1 -> (holder as UtamaHolder).bind(food)
            VIEW_TYPE_LAYOUT_2 -> (holder as ExploreHolder).bind(food)
            VIEW_TYPE_LAYOUT_3 -> (holder as FavoriteHolder).bind(food)
            VIEW_TYPE_LAYOUT_4 -> (holder as ResepHolder).bind(food)
        }
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(food)
        }
    }

    private inner class ExploreHolder(val binding: ItemFood2LayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food) {
            binding.imgFood.setImageResource(food.url)
            binding.namaFood.text = food.name
            binding.timeCook.text = food.time
        }
    }

    private inner class UtamaHolder(val binding: ItemFoodLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food) {
            binding.imgFood.setImageResource(food.url)
            binding.nameFood.text = food.name
        }
    }

    private inner class FavoriteHolder(val binding: ItemFavoriteLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food) {
            binding.imgFav.setImageResource(food.url)
            binding.nameFood.text = food.name
        }
    }

    private inner class ResepHolder(val binding: ItemResepLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food?) {
        }
    }

    interface OnItemClickListener {
        fun onItemClick(food: Food)
    }
}