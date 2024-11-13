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
        
        arguments?.getString("imageUri")?.let { uri ->
            binding.plantImage.setImageURI(Uri.parse(uri))
            getPlantDetails()
        }
    }

    private fun getPlantDetails() {
        binding.progressBar.isVisible = true
        
        lifecycleScope.launch {
            try {
                val response = ApiConfig.getApiService().scanPlant()
                if (response.isSuccessful) {
                    response.body()?.let { plantResponse ->
                        binding.apply {
                            plantName.text = plantResponse.plant
                            diseaseName.text = plantResponse.disease
                            plantDetails.text = plantResponse.details
                        }
                    }
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                binding.progressBar.isVisible = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
