package com.example.carsparts.data

import com.example.carsparts.domain.entity.CarEntity
import com.example.carsparts.domain.entity.PartEntity

sealed class ImportResult {
    data class CarImported(val car: CarEntity) : ImportResult()
    data class PartImported(val part: PartEntity, val carId: Int?) : ImportResult()
}