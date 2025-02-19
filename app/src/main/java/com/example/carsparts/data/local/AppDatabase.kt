package com.example.carsparts.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.carsparts.domain.entity.*

@Database(
    entities = [CarEntity::class, PartEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun partDao(): PartDao
}