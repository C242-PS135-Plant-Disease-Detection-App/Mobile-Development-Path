package com.altaf.plantanist.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.altaf.plantanist.api.Prediction
import com.altaf.plantanist.databinding.ItemHistoryBinding
import com.bumptech.glide.Glide

class HistoryAdapter(private val predictions: List<Prediction>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(prediction: Prediction) {
            binding.plantNameTextView.text = prediction.plant
            binding.conditionTextView.text = prediction.condition
            Glide.with(binding.root.context)
                .load(prediction.image_url)
                .centerCrop()
                .into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(predictions[position])
    }

    override fun getItemCount(): Int = predictions.size
}