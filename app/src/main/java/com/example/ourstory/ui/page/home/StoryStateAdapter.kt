package com.example.ourstory.ui.page.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ourstory.databinding.LoadPagerDataBinding

class StoryStateAdapter(private val reload: () -> Unit) :
    LoadStateAdapter<StoryStateAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding =
            LoadPagerDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, reload)
    }

    class ViewHolder(private val binding: LoadPagerDataBinding, reload: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnReload.setOnClickListener { reload.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.shimmerViewContainer.isVisible = loadState is LoadState.Loading
            binding.btnReload.isVisible = loadState is LoadState.Error
        }
    }
}