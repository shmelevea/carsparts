package com.example.carsparts.di

import com.example.carsparts.data.local.CarDao
import com.example.carsparts.data.local.PartDao
import com.example.carsparts.data.repository.CarRepositoryImpl
import com.example.carsparts.data.repository.PartRepositoryImpl
import com.example.carsparts.domain.repository.CarRepository
import com.example.carsparts.domain.repository.PartRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCarRepository(carDao: CarDao): CarRepository {
        return CarRepositoryImpl(carDao)
    }

    @Provides
    @Singleton
    fun providePartRepository(partDao: PartDao): PartRepository {
        return PartRepositoryImpl(partDao)
    }
}