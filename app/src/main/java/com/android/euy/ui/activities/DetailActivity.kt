package com.android.euy.ui.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.android.euy.R
import com.android.euy.data.model.Recipe
import com.android.euy.databinding.ActivityDetailBinding
import com.android.euy.ui.viewmodels.RecipeViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("EUYPref", Context.MODE_PRIVATE)

        val userId = sharedPreferences.getString("userId","")
        val token = sharedPreferences.getString("token","")

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val recipe = intent.getSerializableExtra("recipe") as Recipe

        recipe.let {
            if(!recipe.image.isNullOrEmpty()){
                Glide.with(this).load(recipe.image)
                    .into(binding.ivDetailRecipe)
            }else{
                binding.ivDetailRecipe.setImageResource(R.drawable.placeholder_img)
            }

            binding.tvRecipeName.text= recipe.name
            binding.tvDescription.text = recipe.description.ifEmpty { "-" }
            binding.tvSteps.text = recipe.steps.joinToString()
            binding.tvTotalBahan.text = recipe.ingredients.size.toString() + " Bahan"
            binding.tvBahan.text= recipe.ingredients.joinToString("\n")

            if (userId != null && token!=null) {
                Log.e("userId",userId)
                Log.e("recipeId",recipe.id)
                Log.e("token",token)
                viewModel.postViewedRecipe(userId,recipe.id,token)
            }

        }

    }
}