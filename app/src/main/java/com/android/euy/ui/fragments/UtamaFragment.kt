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
import com.android.euy.databinding.FragmentUtamaBinding
import com.android.euy.ui.activities.DetailActivity
import com.android.euy.ui.activities.FavouriteActivity
import com.android.euy.ui.adapters.MakananAdapter

class UtamaFragment : Fragment(),MakananAdapter.OnItemClickListener {

    private var _binding: FragmentUtamaBinding? = null
    private val binding get() = _binding!!

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
        var adapter1 = MakananAdapter(this@UtamaFragment.requireContext(),1, dummy.makananSehatList)
        adapter1.setOnItemClickListener(this)

        var adapter2 = MakananAdapter(this@UtamaFragment.requireContext(),1, dummy.nasiGorengList)
        adapter2.setOnItemClickListener(this)

        binding.rvMakananSehat.setHasFixedSize(true)
        binding.rvMakananSehat.layoutManager = GridLayoutManager(this@UtamaFragment.requireContext(), 3)
        binding.rvMakananSehat.adapter = adapter2

        binding.rvNasiGoreng.setHasFixedSize(true)
        binding.rvNasiGoreng.layoutManager = GridLayoutManager(this@UtamaFragment.requireContext(), 3)
        binding.rvNasiGoreng.adapter = adapter1

        binding.btnFav.setOnClickListener {
            startActivity(Intent(this@UtamaFragment.context,FavouriteActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(food: Food) {
        val intent = Intent(this@UtamaFragment.context, DetailActivity::class.java)
//        intent.putExtra("movie", food)
        startActivity(intent)
    }

}