package com.example.carsparts.data

import com.example.carsparts.domain.entity.CarEntity
import com.example.carsparts.domain.entity.PartEntity
import com.example.carsparts.domain.repository.CarRepository
import com.example.carsparts.domain.repository.PartRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ImportDataUseCase(
    private val carRepository: CarRepository,
    private val partRepository: PartRepository
) {
    suspend operator fun invoke(json: String): ImportResult {
        val type = object : TypeToken<Map<String, Any>>() {}.type
        val data: Map<String, Any> = Gson().fromJson(json, type)

        return when (data["type"]) {
            "car_with_parts" -> {
                // Вызов функции импорта для машины с запчастями
                importCarWithParts(data)

                // Возвращаем результат
                val car = data["car"] as CarEntity
                ImportResult.CarImported(car)
            }
            "part_only" -> {
                val part = data["part"] as PartEntity
                val carId = if (part.carId != 0) part.carId else null
                // Добавить логику импорта для запчасти
                ImportResult.PartImported(part, carId)
            }
            else -> throw IllegalArgumentException("Unknown data type")
        }
    }

    private suspend fun importCarWithParts(data: Map<String, Any>) {
        val carJson = Gson().toJson(data["car"]) // Преобразуем обратно в JSON
        val partsJson = Gson().toJson(data["parts"]) // Преобразуем обратно в JSON

        val car: CarEntity = Gson().fromJson(carJson, CarEntity::class.java)

        val listType = object : TypeToken<List<PartEntity>>() {}.type
        val parts: List<PartEntity> = Gson().fromJson(partsJson, listType)

        // Проверяем, существует ли машина с таким id
        val existingCar = carRepository.getCarById(car.id)
        if (existingCar != null) {
            carRepository.updateCar(car)
        } else {
            carRepository.insertCar(car)
        }

        // Обновляем или вставляем запчасти
        parts.forEach { part ->
            val existingPart = partRepository.getPartById(part.id)
            partRepository.updatePart(part)
        }
    }
}