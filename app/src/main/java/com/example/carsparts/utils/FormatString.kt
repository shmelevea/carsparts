package com.example.carsparts.utils

fun capitalizeFirstLetter(input: String): String {
    if (input.isEmpty()) return input
    return input.first().uppercaseChar() + input.drop(1)
}

fun formatVin(input: String): String {
    return input.uppercase().filter { it.isLetterOrDigit() && it !in listOf('I', 'O', 'Q') }
}

fun formatPart(input: String): String {
    return input.uppercase()
}

fun sanitizeInput(
    input: String,
    maxLength: Int = 30,
    transform: (String) -> String = { it }
): String {
    val sanitized = input.replace("\n", "").take(maxLength)
    return transform(sanitized)
}