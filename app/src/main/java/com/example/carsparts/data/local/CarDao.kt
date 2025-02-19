package com.example.carsparts.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.carsparts.domain.entity.CarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {

    @Query("SELECT * FROM car")
    fun getAllCars(): Flow<List<CarEntity>>

    @Insert
    suspend fun insertCar(car: CarEntity)

    @Update
    suspend fun updateCar(car: CarEntity)

    @Query("DELETE FROM car WHERE id = :carId")
    suspend fun deleteCar(carId: Int)
}