package com.example.cat_and_mise.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.formatServerDate(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = inputFormat.parse(this)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        "Неверный формат даты"
    }
}

fun Date.formatToDisplay(): String {
    val outputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return outputFormat.format(this)
}