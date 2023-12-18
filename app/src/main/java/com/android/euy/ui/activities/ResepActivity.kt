package com.android.euy.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.euy.data.model.Food
import com.android.euy.data.model.Recipe
import com.android.euy.databinding.ActivityResepBinding
import com.android.euy.ui.adapters.ExplorePagingAdapter
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

//        var adapter = ExplorePagingAdapter { _, recipe ->
//            val intent = Intent(this, DetailActivity::class.java)
//            intent.putExtra("recipe", recipe)
//            startActivity(intent)
//        }
        binding.rvResep.setHasFixedSize(true)

        var layoutManager =  LinearLayoutManager(this@ResepActivity)
        binding.rvResep.layoutManager = layoutManager
        binding.rvResep.adapter = adapter

//        binding.rvResep.addOnScrollListener(object : RecyclerView.OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val visibleItemCount = layoutManager.childCount
//                val totalItemCount = layoutManager.itemCount
//                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//
//                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
//                    && firstVisibleItemPosition >= 0
//                ) {
//                    resepList.clear()
//                    viewModel.recipeList.value?.data?.let { resepList.addAll(it.recipes) }
//                    adapter.notifyDataSetChanged()
//                }
//            }
//        })

        viewModel.recipeList.observe(this) {
            if (it != null) {
                binding.progressBar.visibility = View.GONE
                if (it.data.recipes.isEmpty()){
                    binding.noDataFoundText.isVisible = true
                    binding.rvResep.isVisible = false
                }else{
                    binding.noDataFoundText.isVisible = false
                    binding.rvResep.isVisible = true

                    resepList.clear()
                    resepList.addAll(it.data.recipes.subList(0,it.data.recipes.size/2))
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onItemClick(recipe: Recipe) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("recipe", recipe)
        startActivity(intent)
    }
}