package com.altaf.plantanist.ui.details

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ApiConfig
import com.altaf.plantanist.databinding.FragmentDetailsBinding
import kotlinx.coroutines.launch
import androidx.core.content.ContextCompat
import com.altaf.plantanist.data.PlantResponse

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        displayPlantDetails()
    }

    private fun displayPlantDetails() {
        arguments?.let { args ->
            val imageUri = args.getString("imageUri")
            val scanResult = args.getParcelable("scanResult", PlantResponse::class.java)
            
            imageUri?.let { uri ->
                binding.plantImage.setImageURI(Uri.parse(uri))
            }
            
            scanResult?.let { result ->
                binding.apply {
                    plantName.text = result.plant
                    diseaseName.text = result.disease
                    plantDetails.text = result.details
                    
                    when {
                        result.confidence < 0.4f -> {
                            confidenceText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
                            confidenceText.text = "Low confidence result. Consider rescanning with better lighting and focus for more accurate results."
                        }
                        result.confidence < 0.7f -> {
                            confidenceText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_orange_dark))
                            confidenceText.text = "Medium confidence result. Results might not be fully accurate."
                        }
                        else -> {
                            confidenceText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
                            confidenceText.text = "High confidence result"
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
