package com.altaf.plantanist.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.altaf.plantanist.R
import com.altaf.plantanist.databinding.FragmentScanBinding
import java.io.File

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageCapture: ImageCapture

    companion object {
        private const val GALLERY_REQUEST_CODE = 1001
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Create a Bundle to pass the image URI
                val bundle = Bundle().apply {
                    putString("imageUri", it.toString())
                }
                // Navigate to DetailFragment with the Bundle
                findNavController().navigate(R.id.navigation_detail, bundle)
            }
        }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Camera permission is required to use this feature.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.galleryButton.setOnClickListener {
            openGallery()
        }

        binding.cameraCaptureButton.setOnClickListener {
            captureImage()
        }

        return root
    }

    override fun onStart() {
        super.onStart()
        checkCameraPermission()
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

                imageCapture = ImageCapture.Builder().build()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                e.message?.let {
                    Toast.makeText(requireContext(), "Error starting camera: $it", Toast.LENGTH_SHORT).show()
                }
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun captureImage() {
        val photoFile = File(requireContext().getExternalFilesDir(null), "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // Create a Bundle to pass the image URI
                    val bundle = Bundle().apply {
                        putString("imageUri", photoFile.toURI().toString())
                    }
                    // Navigate to DetailFragment with the Bundle
                    findNavController().navigate(R.id.navigation_detail, bundle)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Error capturing image: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
