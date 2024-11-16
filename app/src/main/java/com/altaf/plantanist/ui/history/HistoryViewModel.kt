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


}
