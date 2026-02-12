package com.example.carsparts.utils

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.carsparts.domain.usecase.ExportDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

suspend fun saveCarWithPartsToFile(
    context: Context,
    carId: Int,
    vin: String,
    exportDataUseCase: ExportDataUseCase
): File? = withContext(Dispatchers.IO) {
    try {
        val jsonData = exportDataUseCase.exportCarWithParts(carId)

        val file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveToDownloadsScoped(context, "car_$vin.json", jsonData)
        } else {
            val directory = getDownloadFolderLegacy() ?: return@withContext null
            val file = File(directory, "car_$vin.json")
            file.writeText(jsonData)
            file
        }

        file
    } catch (_: Exception) {
        null
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
private fun saveToDownloadsScoped(context: Context, fileName: String, data: String): File? {
    val contentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        put(MediaStore.Downloads.MIME_TYPE, "application/json")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/CarsParts")
    }

    val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        ?: return null

    return try {
        contentResolver.openOutputStream(uri)?.use { it.write(data.toByteArray()) }
        File(uri.path ?: return null)
    } catch (_: Exception) {
        null
    }
}

private fun getDownloadFolderLegacy(): File? {
    val downloadsDirectory =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    val carsPartsDirectory = File(downloadsDirectory, "CarsParts")
    if (!carsPartsDirectory.exists() && !carsPartsDirectory.mkdirs()) {
        return null
    }

    return carsPartsDirectory
}

