package com.example.carsparts.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

import android.os.Parcelable
import com.example.carsparts.utils.toLocalDateOrNull
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

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

val PartEntity.parsedReplacementDate: LocalDate?
    get() = replacementDate.toLocalDateOrNull()
