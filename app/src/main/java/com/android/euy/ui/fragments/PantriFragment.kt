package com.android.euy.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.android.euy.databinding.FragmentPantriBinding
import com.android.euy.ui.activities.ResepActivity
import com.android.euy.ui.adapters.BahanAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent


class PantriFragment : Fragment() {
    private var _binding: FragmentPantriBinding? = null
    private val binding get() = _binding!!
    private val listBahan = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPantriBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP
        val adapter = BahanAdapter(listBahan)
        binding.rvPantri.layoutManager = layoutManager
        binding.rvPantri.adapter = adapter

        binding.btnLihatResep.setOnClickListener {
            val intent = Intent(this@PantriFragment.context,ResepActivity::class.java)
            intent.putExtra("bahan",listBahan.joinToString())
            startActivity(intent)
        }

        binding.edtTxtBahan.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val newItem = binding.edtTxtBahan.text.toString().trim()

                if (newItem.isNotEmpty()) {
                    listBahan.add(newItem)
                    adapter.notifyDataSetChanged()
                    binding.edtTxtBahan.text.clear()
                }
            }
            true
        }

//        binding.edtTxtBahan.setOnKeyListener { _, keyCode, event ->
//            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER ) {
//                val newItem = binding.edtTxtBahan.text.toString().trim()
//
//                if (newItem.isNotEmpty()) {
//                    listBahan.add(newItem)
//                    adapter.notifyDataSetChanged()
//                    binding.edtTxtBahan.text.clear()
//                }
//                return@setOnKeyListener true
//            }
//            false
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}