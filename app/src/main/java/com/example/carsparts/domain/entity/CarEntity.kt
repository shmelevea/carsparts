package com.example.carsparts.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car")
data class CarEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val brand: String,
    val model: String,
    val year: Int?,
    val vin: String
)