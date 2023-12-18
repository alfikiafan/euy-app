package com.android.euy.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.android.euy.R
import com.android.euy.data.model.Recipe
import com.android.euy.databinding.FragmentExploreBinding
import com.android.euy.ui.activities.DetailActivity
import com.android.euy.ui.adapters.ExplorePagingAdapter
import com.android.euy.ui.adapters.LoadStateAdapter
import com.android.euy.ui.adapters.MakananAdapter
import com.android.euy.ui.viewmodels.ExplorePagingViewModel
import kotlinx.coroutines.flow.collect


class ExploreFragment : Fragment(),MakananAdapter.OnItemClickListener {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private val pagingViewModel: ExplorePagingViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("EUYPref", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token","")
        token?.let{
            val adapter = ExplorePagingAdapter { _, recipe ->
                val intent = Intent(requireActivity(), DetailActivity::class.java)
                intent.putExtra("recipe", recipe)
                startActivity(intent)
            }

            adapter.addLoadStateListener { loadState ->
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                    binding.rvExplore.isVisible = false
                    binding.noDataFoundText.isVisible = true
                } else {
                    binding.rvExplore.isVisible = true
                    binding.noDataFoundText.isVisible = false
                }
            }

            val layoutManager = GridLayoutManager(context, 2)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == adapter.itemCount  && adapter.itemCount > 0) {
                        2
                    } else {
                        1
                    }
                }
            }

            binding.rvExplore.layoutManager = layoutManager
            binding.rvExplore.adapter = adapter.withLoadStateHeaderAndFooter(
                LoadStateAdapter { adapter.retry() },
                LoadStateAdapter { adapter.retry() })

            pagingViewModel.getExplorePaging(token!!,"").observe(viewLifecycleOwner) {
                adapter.submitData(lifecycle, it)
//                if (adapter.itemCount<1){
//                    binding.noDataFoundText.visibility = View.VISIBLE
//                }else{
//                    binding.noDataFoundText.visibility = View.GONE
//                }
            }

            binding.searchEditText.edtTxtSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val newItem = binding.searchEditText.edtTxtSearch.text.toString().trim()

                    if (newItem.isNotEmpty()) {
                        pagingViewModel.getExplorePaging(token,newItem).observe(viewLifecycleOwner) {
                            adapter.submitData(lifecycle, it)
//                            if (adapter.itemCount<1){
//                                binding.noDataFoundText.visibility = View.VISIBLE
//                            }else{
//                                binding.noDataFoundText.visibility = View.GONE
//                            }
                        }
                    }
                }
                true
            }
//            binding.searchEditText.edtTxtSearch.setOnKeyListener { _, keyCode, event ->
//                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                    val newItem = binding.searchEditText.edtTxtSearch.text.toString().trim()
//
//                    if (newItem.isNotEmpty()) {
//                        pagingViewModel.getExplorePaging(token,newItem).observe(viewLifecycleOwner) {
//                            adapter.submitData(lifecycle, it)
//                        }
//                    }
//                    return@setOnKeyListener true
//                }
//                false
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(food: Recipe) {
        val intent = Intent(this@ExploreFragment.context, DetailActivity::class.java)
        intent.putExtra("recipe", food)
        startActivity(intent)
    }
}