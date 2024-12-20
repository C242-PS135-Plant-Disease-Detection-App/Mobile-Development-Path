package com.altaf.plantanist.ui.scan

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.altaf.plantanist.R
import com.altaf.plantanist.api.ApiConfig
import com.altaf.plantanist.api.Prediction
import com.altaf.plantanist.data.AuthenticationDatabase
import com.altaf.plantanist.databinding.FragmentScanBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageCapture: ImageCapture
    private val scanViewModel: ScanViewModel by viewModels()

    companion object {
        private const val GALLERY_REQUEST_CODE = 1001
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                processImageUri(it)
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

    private val mediaPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                openGallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Media access permission is required to use this feature.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.galleryButton.setOnClickListener {
            checkMediaPermission()
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

    private fun checkMediaPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            mediaPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
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
                    processImageFile(photoFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Error capturing image: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun processImageUri(uri: Uri) {
        val file = getFileFromUri(uri)
        if (file != null) {
            processImageFile(file)
        } else {
            Toast.makeText(requireContext(), "Error processing image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        val contentResolver: ContentResolver = requireContext().contentResolver
        val fileName = getFileName(uri)
        val tempFile = File(requireContext().cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(tempFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return tempFile
    }

    private fun getFileName(uri: Uri): String {
        var name = ""
        val cursor: Cursor? = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        return name
    }

    private fun processImageFile(file: File) {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            val token = AuthenticationDatabase.getDatabase(requireContext()).tokenDao().getToken()?.token ?: return@launch
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
            val response = ApiConfig.getApiService().predictPlant(token, body)
            binding.progressBar.visibility = View.GONE
            if (response.isSuccessful) {
                val prediction = response.body()
                val bundle = Bundle().apply {
                    putString("plant", prediction?.plant)
                    putString("condition", prediction?.condition)
                    putString("description", prediction?.description)
                    putDouble("confidence_score", prediction?.confidence_score ?: 0.0)
                    putString("date", prediction?.date)
                    putString("image_url", prediction?.image_url)
                }
                findNavController().navigate(R.id.navigation_detail, bundle)
            } else {
                Toast.makeText(requireContext(), "Error predicting plant: ${response.message()}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}