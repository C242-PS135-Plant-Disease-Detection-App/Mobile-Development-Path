package com.altaf.plantanist.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Authentication::class], version = 1)
abstract class AuthenticationDatabase : RoomDatabase() {
    abstract fun tokenDao(): AuthenticationDao

    companion object {
        @Volatile
        private var INSTANCE: AuthenticationDatabase? = null

        fun getDatabase(context: Context): AuthenticationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AuthenticationDatabase::class.java,
                    "token_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 