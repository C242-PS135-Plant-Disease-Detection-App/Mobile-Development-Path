package com.altaf.plantanist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AuthenticationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(authentication: Authentication)

    @Query("SELECT * FROM token_table LIMIT 1")
    suspend fun getToken(): Authentication?

    @Query("DELETE FROM token_table")
    suspend fun deleteToken()
} 