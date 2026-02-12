package com.example.carsparts.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy")

fun String.toLocalDateOrNull(): LocalDate? =
    if (this.isBlank()) null
    else runCatching {
        LocalDate.parse(this, DATE_FORMATTER)
    }.getOrNull()