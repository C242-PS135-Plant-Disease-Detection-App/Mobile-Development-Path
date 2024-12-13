package com.altaf.plantanist.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.altaf.plantanist.api.Prediction
import com.altaf.plantanist.databinding.ItemHistoryBinding
import com.bumptech.glide.Glide
import com.altaf.plantanist.R

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

            binding.root.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("plant", prediction.plant)
                    putString("condition", prediction.condition)
                    putString("description", prediction.description)
                    putDouble("confidence_score", prediction.confidence_score)
                    putString("date", prediction.date)
                    putString("image_url", prediction.image_url)
                }
                it.findNavController().navigate(R.id.navigation_detail, bundle)
            }
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