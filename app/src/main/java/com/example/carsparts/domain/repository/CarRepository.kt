package com.example.carsparts.domain.repository

import com.example.carsparts.domain.entity.CarEntity
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    fun getAllCars(): Flow<List<CarEntity>>
    suspend fun insertCar(car: CarEntity)
    suspend fun updateCar(car: CarEntity)
    suspend fun deleteCar(carId: Int)
}