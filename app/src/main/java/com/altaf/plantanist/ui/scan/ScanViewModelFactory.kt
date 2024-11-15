package com.altaf.plantanist.ui.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.altaf.plantanist.data.HistoryDao

class ScanViewModelFactory(
    private val historyDao: HistoryDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScanViewModel(historyDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
