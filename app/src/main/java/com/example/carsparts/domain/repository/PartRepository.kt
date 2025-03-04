package com.example.carsparts.domain.repository

import com.example.carsparts.domain.entity.PartEntity
import kotlinx.coroutines.flow.Flow

interface PartRepository {
    fun getPartsForCar(carId: Int): Flow<List<PartEntity>>
    fun getPartById(partId: Int): Flow<PartEntity?>
    suspend fun insertPart(part: PartEntity)
    suspend fun updatePart(part: PartEntity)
    suspend fun deletePart(partId: Int)
    suspend fun getAllPartIds(): List<Int>
}