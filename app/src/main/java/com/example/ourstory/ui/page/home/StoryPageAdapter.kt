package com.example.ourstory.ui.page.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ourstory.R
import com.example.ourstory.databinding.ItemStoryBinding
import com.example.ourstory.domain.model.StoryModel
import com.example.ourstory.utils.convertDate
import com.example.ourstory.utils.getAddressName
import com.example.ourstory.utils.setImage
import com.google.android.gms.maps.model.LatLng

class StoryPageAdapter(private val context: Context) :
    PagingDataAdapter<StoryModel, StoryPageAdapter.ViewHolder>(differCallback) {


    companion object {
        val differCallback = object : DiffUtil.ItemCallback<StoryModel>() {
            override fun areItemsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean =
                oldItem == newItem

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) holder.bind(currentItem, listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryModel, listener: ((StoryModel) -> Unit)?) {
            with(binding) {
                ivPhoto.setImage(data.photoUrl)

                tvCreated.convertDate(data.createdAt, context)
                tvName.text = data.name
                if (data.lat != null && data.lon != null)
                    tvLocation.text = getAddressName(context, LatLng(data.lat, data.lon))
                else
                    tvLocation.text = context.getString(R.string.unknown)

                itemStory.setOnClickListener {
                    listener?.let { listener(data) }
                }
            }
        }
    }

    private var listener: ((StoryModel) -> Unit)? = null
    fun setOnItemClick(listener: (StoryModel) -> Unit) {
        this.listener = listener
    }


}