package com.example.carsparts.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

fun String.toLocalDateOrNull(): LocalDate? {
    if (this.isBlank()) return null
    val formats = listOf(
        "dd.MM.yyyy",
        "d.M.yyyy",
        "dd-MM-yyyy",
        "d-M-yyyy",
        "yyyy-MM-dd"
    ).map { DateTimeFormatter.ofPattern(it) }

    for (fmt in formats) {
        runCatching {
            return LocalDate.parse(this, fmt)
        }
    }
    return null
}