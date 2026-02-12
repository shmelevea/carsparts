package com.example.carsparts.di

import com.example.carsparts.domain.usecase.ExportDataUseCase
import com.example.carsparts.domain.usecase.ImportDataUseCase
import com.example.carsparts.domain.repository.CarRepository
import com.example.carsparts.domain.repository.PartRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideExportDataUseCase(
        carRepository: CarRepository,
        partRepository: PartRepository
    ): ExportDataUseCase {
        return ExportDataUseCase(carRepository, partRepository)
    }

    @Provides
    fun provideImportDataUseCase(
        carRepository: CarRepository,
        partRepository: PartRepository
    ): ImportDataUseCase {
        return ImportDataUseCase(carRepository, partRepository)
    }
}