package com.altaf.plantanist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "token_table")
data class Authentication(
    @PrimaryKey val id: Int = 0,
    val token: String,
    val name: String,
    val email: String
)