package com.altaf.plantanist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gambar: String, // URI atau path gambar
    val plant: String,
    val disease: String,
    val details: String
)
