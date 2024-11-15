package com.altaf.plantanist.data

class HistoryRepository(private val historyDao: HistoryDao) {
    val allHistory = historyDao.getAllHistory()

    suspend fun insert(history: HistoryEntity) {
        historyDao.insertHistory(history)
    }
}
