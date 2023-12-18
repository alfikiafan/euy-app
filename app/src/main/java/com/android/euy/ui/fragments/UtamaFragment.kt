package com.android.euy.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.euy.data.model.Food
import com.android.euy.data.model.Recipe
import com.android.euy.data.utils.dummy
import com.android.euy.databinding.FragmentUtamaBinding
import com.android.euy.ui.activities.DetailActivity
import com.android.euy.ui.activities.FavouriteActivity
import com.android.euy.ui.adapters.MakananAdapter
import com.android.euy.ui.viewmodels.AuthViewModel
import com.android.euy.ui.viewmodels.RecipeViewModel

class UtamaFragment : Fragment(),MakananAdapter.OnItemClickListener {

    private var _binding: FragmentUtamaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeViewModel by activityViewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private val chicken = ArrayList<Recipe>()
    private val beef = ArrayList<Recipe>()
    private val lamb = ArrayList<Recipe>()
    private val fish = ArrayList<Recipe>()
    private val resepList = ArrayList<Recipe>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUtamaBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("EUYPref", Context.MODE_PRIVATE)

        binding.progressBar.visibility = View.VISIBLE
        val token =  sharedPreferences.getString("token","")
        viewModel.getOurChoices(token!!)

        var adapterChicken = MakananAdapter(this@UtamaFragment.requireContext(),1, chicken)
        adapterChicken.setOnItemClickListener(this)

        var adapterBeef = MakananAdapter(this@UtamaFragment.requireContext(),1, beef)
        adapterBeef.setOnItemClickListener(this)

        var adapterLamb = MakananAdapter(this@UtamaFragment.requireContext(),1, lamb)
        adapterLamb.setOnItemClickListener(this)

        var adapterFish = MakananAdapter(this@UtamaFragment.requireContext(),1, fish)
        adapterFish.setOnItemClickListener(this)

        var searchAdapter = MakananAdapter(this@UtamaFragment.requireContext(),2, resepList)
        searchAdapter.setOnItemClickListener(this)

        binding.rvChickenRecipe.setHasFixedSize(true)
        binding.rvChickenRecipe.layoutManager = LinearLayoutManager(this@UtamaFragment.requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvChickenRecipe.adapter = adapterChicken

        binding.rvBeefRecipe.setHasFixedSize(true)
        binding.rvBeefRecipe.layoutManager = LinearLayoutManager(this@UtamaFragment.requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvBeefRecipe.adapter = adapterBeef

        binding.rvFishRecipe.setHasFixedSize(true)
        binding.rvFishRecipe.layoutManager = LinearLayoutManager(this@UtamaFragment.requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvFishRecipe.adapter = adapterFish

        binding.rvLambRecipe.setHasFixedSize(true)
        binding.rvLambRecipe.layoutManager = LinearLayoutManager(this@UtamaFragment.requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvLambRecipe.adapter = adapterLamb

        binding.btnFav.setOnClickListener {
            startActivity(Intent(this@UtamaFragment.context,FavouriteActivity::class.java))
        }

        viewModel.ourChoicesList.observe(viewLifecycleOwner) {
            if (it != null) {
                chicken.clear()
                beef.clear()
                lamb.clear()
                fish.clear()
                chicken.addAll(it.data.chicken)
                beef.addAll(it.data.beef)
                lamb.addAll(it.data.lamb)
                fish.addAll(it.data.fish)
                adapterChicken.notifyDataSetChanged()
                adapterBeef.notifyDataSetChanged()
                adapterFish.notifyDataSetChanged()
                adapterLamb.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }
        }

        binding.searchEditText.edtTxtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val newItem = binding.searchEditText.edtTxtSearch.text.toString().trim()
//
                if (newItem.isNotEmpty()) {
                    binding.scrollview.visibility = View.GONE
//                    binding.llChicken.visibility = View.GONE
//                    binding.llFish.visibility = View.GONE
//                    binding.llLamb.visibility = View.GONE
                    binding.rvSearch.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.VISIBLE

                    viewModel.getRecipesFromName(newItem,token)

                    binding.rvSearch.setHasFixedSize(true)
                    binding.rvSearch.layoutManager = GridLayoutManager(this@UtamaFragment.requireContext(), 2)
                    binding.rvSearch.adapter = searchAdapter
                }
            }
            true
        }

        viewModel.searchedList.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.progressBar.visibility = View.GONE
                resepList.clear()
                resepList.addAll(it.data.recipes)
                searchAdapter.notifyDataSetChanged()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(recipe: Recipe) {
        val intent = Intent(this@UtamaFragment.context, DetailActivity::class.java)
        intent.putExtra("recipe", recipe)
        startActivity(intent)
    }

}