package com.android.euy.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.euy.data.model.Food
import com.android.euy.data.model.Recipe
import com.android.euy.databinding.ActivityResepBinding
import com.android.euy.ui.adapters.MakananAdapter
import com.android.euy.ui.viewmodels.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResepActivity : AppCompatActivity(), MakananAdapter.OnItemClickListener {
    private lateinit var binding: ActivityResepBinding
    private val viewModel: RecipeViewModel by viewModels()
    private val resepList = ArrayList<Recipe>()
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResepBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("EUYPref", Context.MODE_PRIVATE)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val bahan = intent.getStringExtra("bahan")
        Log.e("bahan",bahan.toString())
        val token = sharedPreferences.getString("token","")
        Log.e("token",token.toString())

        bahan?.let {
            viewModel.getRecipesFromIngredients(bahan,token.toString())
        }
        var adapter = MakananAdapter(this@ResepActivity,4, resepList)
        adapter.setOnItemClickListener(this)
        binding.rvResep.setHasFixedSize(true)
        binding.rvResep.layoutManager = LinearLayoutManager(this@ResepActivity)
        binding.rvResep.adapter = adapter

        viewModel.recipeList.observe(this) {
            if (it != null) {
                Log.e("RES",it.toString())
                resepList.clear()
                resepList.addAll(it.data.recipes)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onItemClick(recipe: Recipe) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("recipe", recipe)
        startActivity(intent)
    }
}