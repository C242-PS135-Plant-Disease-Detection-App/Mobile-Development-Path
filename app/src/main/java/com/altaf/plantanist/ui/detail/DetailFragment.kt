package com.altaf.plantanist.ui.detail

import android.net.Uri
import android.os.Bundle
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

        // Get the image URI from arguments
        val imageUri: String? = arguments?.getString("imageUri")
        imageUri?.let {
            // Load the image using Glide or any other image loading library
            Glide.with(this)
                .load(Uri.parse(it))
                .into(binding.imageView)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
