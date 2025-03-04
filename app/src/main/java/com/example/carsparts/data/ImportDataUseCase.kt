package com.example.carsparts.data

import com.example.carsparts.domain.entity.CarEntity
import com.example.carsparts.domain.entity.PartEntity
import com.example.carsparts.domain.repository.CarRepository
import com.example.carsparts.domain.repository.PartRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class ImportDataUseCase(
    private val carRepository: CarRepository,
    private val partRepository: PartRepository
) {
    suspend operator fun invoke(json: String) {
        val typeToken = object : TypeToken<Map<String, Any>>() {}.type
        val data: Map<String, Any> = Gson().fromJson(json, typeToken)

        when (data["type"]) {
            "car_with_parts" -> importCarWithParts(data)
            else -> throw IllegalArgumentException("Unknown data type")
        }
    }

    private suspend fun importCarWithParts(data: Map<String, Any>): CarEntity {
        val carJson = Gson().toJson(data["car"])
        val partsJson = Gson().toJson(data["parts"])

        val car: CarEntity = Gson().fromJson(carJson, CarEntity::class.java)
        val listType = object : TypeToken<List<PartEntity>>() {}.type
        val parts: List<PartEntity> = Gson().fromJson(partsJson, listType)

        val existingCar = carRepository.getCarByVin(car.vin)
        val finalCar = if (existingCar != null) {
            car.copy(id = existingCar.id)
        } else {
            val conflictCar = carRepository.getCarById(car.id)
            if (conflictCar != null) {
                car.copy(id = generateNewCarId())
            } else {
                car
            }
        }

        if (existingCar != null) {
            carRepository.updateCar(finalCar)
        } else {
            carRepository.insertCar(finalCar)
        }

        val existingParts = partRepository.getPartsForCar(finalCar.id).first()

        parts.forEach { part ->
            val isDuplicate = existingParts.any {
                it.partNumber == part.partNumber && it.replacementDate == part.replacementDate
            }

            if (!isDuplicate) {
                val newPart = part.copy(id = generateNewPartId(), carId = finalCar.id)
                partRepository.insertPart(newPart)
            }
        }

        return finalCar
    }

    private suspend fun generateNewCarId(): Int {
        var newId: Int
        do {
            newId = (1..Int.MAX_VALUE).random()
        } while (carRepository.getCarById(newId) != null)
        return newId
    }

    private suspend fun generateNewPartId(): Int {
        var newId: Int
        do {
            newId = (1..Int.MAX_VALUE).random()
        } while (partRepository.getPartById(newId).firstOrNull() != null)
        return newId
    }
}