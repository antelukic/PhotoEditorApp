package com.photoeditor.app.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.photoeditor.app.databinding.ItemEffectThumbnailBinding

class EffectsAdapter(
    private val listener: OnFilterClickListener
) : androidx.recyclerview.widget.ListAdapter<EffectsThumbnail, EffectsAdapter.EffectsViewHolder>(EffectsDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EffectsViewHolder =
        EffectsViewHolder(
            ItemEffectThumbnailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: EffectsViewHolder,
        position: Int
    ) {
        val thumbnail = getItem(position)
        holder.effectTitle.text = thumbnail.title
        holder.effectRootView.setOnClickListener {
            listener.onFilterClicked(thumbnail)
        }
    }

    class EffectsViewHolder(binding: ItemEffectThumbnailBinding) : RecyclerView.ViewHolder(binding.root) {
        val effectTitle: TextView = binding.effectName
        val effectRootView: LinearLayout = binding.effectsRootView
    }
}

class EffectsDiffCallback : DiffUtil.ItemCallback<EffectsThumbnail>() {

    override fun areItemsTheSame(oldItem: EffectsThumbnail, newItem: EffectsThumbnail): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: EffectsThumbnail, newItem: EffectsThumbnail): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }
}
