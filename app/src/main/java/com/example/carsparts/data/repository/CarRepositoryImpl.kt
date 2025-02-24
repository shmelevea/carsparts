package com.example.carsparts.data.repository

import com.example.carsparts.data.local.CarDao
import com.example.carsparts.domain.entity.CarEntity
import com.example.carsparts.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
    private val carDao: CarDao
) : CarRepository {
    override fun getAllCars(): Flow<List<CarEntity>> = carDao.getAllCars()

    override suspend fun insertCar(car: CarEntity) = carDao.insertCar(car)

    override suspend fun updateCar(car: CarEntity) = carDao.updateCar(car)

    override suspend fun deleteCar(carId: Int) = carDao.deleteCar(carId)

    override suspend fun getCarById(carId: Int): CarEntity? {
        return carDao.getCarById(carId)
    }
}