package com.altaf.plantanist.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.altaf.plantanist.data.HistoryEntity
import com.altaf.plantanist.data.HistoryRepository
import com.altaf.plantanist.data.PlantDatabase
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val historyDao = PlantDatabase.getDatabase(application).historyDao()
    private val repository = HistoryRepository(historyDao)
    private val apiService = ApiConfig.getApiService()

    val allHistory: LiveData<List<HistoryEntity>> = repository.allHistory.asLiveData()

    // Fungsi untuk memanggil API dan menyimpan hasilnya
    fun scanAndSavePlant() {
        viewModelScope.launch {
            try {
                val response = apiService.scanPlant()
                if (response.isSuccessful) {
                    response.body()?.let { plantResponse ->
                        val history = HistoryEntity(
                            gambar = "path_or_uri", // Ganti dengan URI atau path gambar yang sesuai
                            plant = plantResponse.plant,
                            disease = plantResponse.disease,
                            details = plantResponse.details
                        )
                        repository.insert(history)
                    }
                }
            } catch (e: Exception) {
                // Tangani error sesuai kebutuhan, misalnya dengan log atau notifikasi
            }
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is history Fragment"
    }
    val text: LiveData<String> = _text
}
