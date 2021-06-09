package com.jlocker.itunesartists.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jlocker.itunesartists.data.ArtistProduct
import com.jlocker.itunesartists.databinding.ListItemTrackBinding

class TrackAdapter : PagingDataAdapter<ArtistProduct, TrackAdapter.TrackViewHolder>(TrackDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(
            ListItemTrackBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = getItem(position)
        if (track != null) {
            holder.bind(track)
        }
    }

    class TrackViewHolder(private val binding: ListItemTrackBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArtistProduct) {
            binding.apply {
                track = item
                executePendingBindings()
            }
        }
    }
}

private class TrackDiffCallback : DiffUtil.ItemCallback<ArtistProduct>() {
    override fun areItemsTheSame(oldItem: ArtistProduct, newItem: ArtistProduct): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(oldItem: ArtistProduct, newItem: ArtistProduct): Boolean {
        return oldItem == newItem
    }
}