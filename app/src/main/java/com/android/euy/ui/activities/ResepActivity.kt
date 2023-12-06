package com.android.euy.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.euy.data.model.Food
import com.android.euy.data.utils.dummy
import com.android.euy.databinding.ActivityResepBinding
import com.android.euy.ui.adapters.MakananAdapter

class ResepActivity : AppCompatActivity(), MakananAdapter.OnItemClickListener {
    private lateinit var binding: ActivityResepBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResepBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        var adapter = MakananAdapter(this@ResepActivity,4, dummy.resepList)
        adapter.setOnItemClickListener(this)
        binding.rvResep.setHasFixedSize(true)
        binding.rvResep.layoutManager = LinearLayoutManager(this@ResepActivity)
        binding.rvResep.adapter = adapter
    }

    override fun onItemClick(food: Food) {
        val intent = Intent(this, DetailActivity::class.java)
//        intent.putExtra("movie", food)
        startActivity(intent)
    }
}