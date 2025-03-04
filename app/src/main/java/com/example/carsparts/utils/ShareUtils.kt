package com.example.carsparts.utils

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.example.carsparts.data.ExportDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

suspend fun saveCarWithPartsToFile(
    context: Context,
    carId: Int,
    exportDataUseCase: ExportDataUseCase
): File? = withContext(Dispatchers.IO) {
    try {
        val jsonData = exportDataUseCase.exportCarWithParts(carId)
        val carsPartsDirectory = getDownloadFolder(context) ?: return@withContext null
        val file = File(carsPartsDirectory, "car_$carId.json")
        file.writeText(jsonData)
        file
    } catch (e: Exception) {
        null
    }
}

fun getDownloadFolder(context: Context): File? {
    val downloadsDirectory =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    val carsPartsDirectory = File(downloadsDirectory, "CarsParts")
    if (!carsPartsDirectory.exists()) {
        if (!carsPartsDirectory.mkdirs()) {
            Toast.makeText(context, "Failed to create CarsParts directory", Toast.LENGTH_SHORT)
                .show()
            return null
        }
    }

    return carsPartsDirectory
}

