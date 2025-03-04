package com.example.carsparts.data.repository

import com.example.carsparts.data.local.PartDao
import com.example.carsparts.domain.entity.PartEntity
import com.example.carsparts.domain.repository.PartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class PartRepositoryImpl @Inject constructor(
    private val partDao: PartDao
) : PartRepository {

    override fun getPartsForCar(carId: Int): Flow<List<PartEntity>> {
        return partDao.getPartsForCar(carId)
    }

    override fun getPartById(partId: Int): Flow<PartEntity?> {
        return partDao.getPartById(partId)
    }

    override suspend fun insertPart(part: PartEntity) {
        partDao.insertPart(part)
    }

    override suspend fun updatePart(part: PartEntity) {
        partDao.updatePart(part)
    }

    override suspend fun deletePart(partId: Int) {
        partDao.deletePart(partId)
    }

    override suspend fun getAllPartIds(): List<Int> {
        return partDao.getAllPartIds().firstOrNull() ?: emptyList()
    }
}