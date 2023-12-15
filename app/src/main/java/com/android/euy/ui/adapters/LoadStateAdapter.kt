package com.android.euy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.euy.databinding.LoadStateLayoutBinding

class LoadStateAdapter(private val callback: View.OnClickListener) : LoadStateAdapter<com.android.euy.ui.adapters.LoadStateAdapter.ViewHolder>() {

  inner class ViewHolder(val binding: LoadStateLayoutBinding) : RecyclerView.ViewHolder(binding.root)


  override fun onBindViewHolder(holder: com.android.euy.ui.adapters.LoadStateAdapter.ViewHolder, loadState: LoadState) {
    with(holder) {
      if (loadState is Error) {
        val loadError = loadState as Error
        binding.textError.text = loadError.localizedMessage
      }
      binding.progressBar.visibility = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
      binding.btnRetry.visibility = if (loadState is Error) View.VISIBLE else View.GONE
      binding.textError.visibility = if (loadState is Error) View.VISIBLE else View.GONE

      binding.btnRetry.setOnClickListener {
        callback
      }
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    loadState: LoadState
  ): ViewHolder {
    val binding = LoadStateLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false)
    return ViewHolder(binding)
  }
}
