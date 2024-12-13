package com.altaf.plantanist.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.altaf.plantanist.databinding.FragmentDetailBinding
import com.bumptech.glide.Glide

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val plant = arguments?.getString("plant")
        val disease = arguments?.getString("disease")
        val description = arguments?.getString("description")
        val confidenceScore = arguments?.getDouble("confidence_score")
        val imageUrl = arguments?.getString("image_url")

        binding.plantTextView.text = plant
        binding.diseaseTextView.text = disease
        binding.descriptionTextView.text = description

        val confidenceMessage = when {
            confidenceScore != null && confidenceScore >= 90 -> {
                binding.confidenceTextView.setTextColor(Color.GREEN)
                "We are confident with this prediction"
            }
            confidenceScore != null && confidenceScore >= 70 -> {
                binding.confidenceTextView.setTextColor(Color.YELLOW)
                "We are less confident with this prediction"
            }
            else -> {
                binding.confidenceTextView.setTextColor(Color.RED)
                "We are not confident with this prediction"
            }
        }
        binding.confidenceTextView.text = confidenceMessage

        // Display the image
        Glide.with(this)
            .load(imageUrl)
            .centerCrop()
            .into(binding.imageView)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}