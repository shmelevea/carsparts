package com.example.carsparts.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "part")
data class PartEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val carId: Int,
    val partNumber: String,
    val secondPartNumber: String,
    val name: String,
    val purchaseDate: String,
    val replacementDate: String,
    val replacementCost: Int,
    val partCost: Int,
    val store: String,
    val mileageKm: Int,
    val breakdownMileageKm: Int,
    val description: String
) : Parcelable
