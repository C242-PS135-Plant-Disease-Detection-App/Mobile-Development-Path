package com.altaf.plantanist.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.altaf.plantanist.R
import com.altaf.plantanist.data.HistoryEntity
import com.altaf.plantanist.databinding.ItemHistoryBinding
import com.bumptech.glide.Glide

class HistoryAdapter : ListAdapter<HistoryEntity, HistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryEntity) {
            // Set teks
            binding.textPlant.text = history.plant
            binding.textDisease.text = history.disease
            binding.textDetails.text = history.details

            // Load gambar menggunakan Glide
            Glide.with(binding.root.context)
                .load(history.gambar) // URI atau path gambar
                .into(binding.imageGambar) // Masukkan ke ImageView
        }
    }

    class HistoryDiffCallback : DiffUtil.ItemCallback<HistoryEntity>() {
        override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem.id == newItem.id // Assuming HistoryEntity has a unique id
        }

        override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem == newItem
        }
    }
}

