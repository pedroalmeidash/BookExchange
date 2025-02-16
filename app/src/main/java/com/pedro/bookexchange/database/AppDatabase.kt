package com.pedro.bookexchange.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Book::class, Proposal::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}