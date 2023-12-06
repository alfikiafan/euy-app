package com.android.euy.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.android.euy.data.model.Food
import com.android.euy.data.utils.dummy
import com.android.euy.databinding.FragmentExploreBinding
import com.android.euy.ui.activities.DetailActivity
import com.android.euy.ui.adapters.MakananAdapter


class ExploreFragment : Fragment(),MakananAdapter.OnItemClickListener {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExploreBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var adapter = MakananAdapter(this@ExploreFragment.requireContext(),2, dummy.exploreFoodList)
        adapter.setOnItemClickListener(this)
        binding.rvExplore.setHasFixedSize(true)
        binding.rvExplore.layoutManager = GridLayoutManager(this@ExploreFragment.requireContext(), 2)
        binding.rvExplore.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(food: Food) {
        val intent = Intent(this@ExploreFragment.context, DetailActivity::class.java)
//        intent.putExtra("movie", food)
        startActivity(intent)
    }
}