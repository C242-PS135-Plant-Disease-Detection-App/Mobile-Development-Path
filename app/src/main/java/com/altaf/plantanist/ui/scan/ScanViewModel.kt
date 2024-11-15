package com.altaf.plantanist.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altaf.plantanist.data.HistoryDao
import com.altaf.plantanist.data.HistoryEntity
import com.altaf.plantanist.data.PlantResponse
import kotlinx.coroutines.launch

class ScanViewModel(private val historyDao: HistoryDao) : ViewModel() {
    private val _scanResult = MutableLiveData<PlantResponse>()
    val scanResult: LiveData<PlantResponse> = _scanResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun scanPlant(imageUri: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService().scanPlant()
                if (response.isSuccessful) {
                    response.body()?.let { result ->
                        _scanResult.value = result
                        // Save result to history with imageUri
                        val history = HistoryEntity(
                            gambar = imageUri,  // Save the image URI here
                            plant = result.plant,
                            disease = result.disease,
                            details = result.details
                        )
                        historyDao.insertHistory(history)
                    }
                } else {
                    _error.value = "Failed to scan plant"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetScanResult() {
        _scanResult.value = null
    }
}
