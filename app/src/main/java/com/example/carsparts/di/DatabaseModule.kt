package com.example.carsparts.di

import android.content.Context
import androidx.room.Room
import com.example.carsparts.data.local.AppDatabase
import com.example.carsparts.data.local.CarDao
import com.example.carsparts.data.local.PartDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val CARS_PARTS_DATABASE = "cars_parts_database"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            CARS_PARTS_DATABASE
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCarDao(database: AppDatabase): CarDao = database.carDao()

    @Provides
    fun providePartDao(database: AppDatabase): PartDao = database.partDao()
}