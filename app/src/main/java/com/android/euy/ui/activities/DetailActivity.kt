package com.android.euy.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.euy.data.model.Recipe
import com.android.euy.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val recipe = intent.getSerializableExtra("recipe") as Recipe

        recipe?.let {
            Glide.with(this).load(recipe.image)
                .into(binding.ivDetailRecipe)
            binding.tvRecipeName.text= recipe.name
            binding.tvDescription.text = recipe.description
            binding.tvSteps.text = recipe.steps.joinToString()
            binding.tvTotalBahan.text = recipe.ingredients.size.toString() + " Bahan"
            binding.tvBahan.text= recipe.ingredients.joinToString("\n")
        }

    }
}