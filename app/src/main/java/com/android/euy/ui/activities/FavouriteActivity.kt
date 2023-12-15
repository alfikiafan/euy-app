package com.android.euy.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.euy.R
import com.android.euy.data.model.Food
import com.android.euy.data.model.Recipe
import com.android.euy.data.utils.dummy
import com.android.euy.databinding.ActivityFavouriteBinding
import com.android.euy.ui.adapters.MakananAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteActivity : AppCompatActivity(),MakananAdapter.OnItemClickListener {
    private lateinit var binding: ActivityFavouriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        var adapter = MakananAdapter(this@FavouriteActivity,3, ArrayList())
        adapter.setOnItemClickListener(this)
        binding.rvFav.setHasFixedSize(true)
        binding.rvFav.layoutManager = LinearLayoutManager(this@FavouriteActivity)
        binding.rvFav.adapter = adapter
    }

    override fun onItemClick(recipe: Recipe) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("recipe", recipe)
        startActivity(intent)
    }
}