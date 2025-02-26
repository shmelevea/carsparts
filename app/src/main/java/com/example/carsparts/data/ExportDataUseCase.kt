package com.example.carsparts.data

import com.example.carsparts.domain.repository.CarRepository
import com.example.carsparts.domain.repository.PartRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.firstOrNull

class ExportDataUseCase(
    private val carRepository: CarRepository,
    private val partRepository: PartRepository
) {
    suspend fun exportCarWithParts(carId: Int): String {
        val car = carRepository.getCarById(carId) ?: throw IllegalArgumentException("Car not found")
        val parts = partRepository.getPartsForCar(carId).firstOrNull() ?: emptyList()

        return Gson().toJson(mapOf("type" to "car_with_parts", "car" to car, "parts" to parts))
    }

    suspend fun exportPart(partId: Int): String {
        val part = partRepository.getPartById(partId).firstOrNull()
            ?: throw IllegalArgumentException("Part not found")

        return Gson().toJson(mapOf("type" to "part_only", "part" to part))
    }
}
