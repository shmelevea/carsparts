package com.example.carsparts.utils

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.example.carsparts.data.ExportDataUseCase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun getDownloadFolder(context: Context): File? {
    // Путь к папке Downloads
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

suspend fun savePartToFile(
    context: Context,
    partId: Int,
    exportDataUseCase: ExportDataUseCase
): File? {
    return try {
        val jsonData = exportDataUseCase.exportPart(partId)
        val carsPartsDirectory = getDownloadFolder(context) ?: return null
        val file = File(carsPartsDirectory, "part_$partId.json")
        saveJsonToFile(file, jsonData)
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun saveCarWithPartsToFile(
    context: Context,
    carId: Int,
    exportDataUseCase: ExportDataUseCase
): File? {
    return try {
        val jsonData =
            exportDataUseCase.exportCarWithParts(carId)
        val carsPartsDirectory = getDownloadFolder(context) ?: return null
        val file = File(carsPartsDirectory, "car_$carId.json")
        saveJsonToFile(file, jsonData)
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun saveJsonToFile(file: File, jsonData: String) {
    try {
        FileOutputStream(file).use { outputStream ->
            outputStream.write(jsonData.toByteArray())
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}