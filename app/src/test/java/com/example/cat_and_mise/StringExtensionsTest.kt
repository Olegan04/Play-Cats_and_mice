package com.example.cat_and_mise

import com.example.cat_and_mise.extensions.formatServerDate
import junit.framework.TestCase.assertEquals
import org.junit.Test

class StringExtensionsTest {
    @Test
    fun testFormatServerDate() {
        // Given
        val inputDate = "2023-10-25"

        // When
        val result = inputDate.formatServerDate()

        // Then
        assertEquals("25.10.2023", result)
    }

    @Test
    fun testFormatServerDateWithInvalidInput() {
        // Given
        val invalidDate = "invalid-date"

        // When
        val result = invalidDate.formatServerDate()

        // Then
        assertEquals("Неверный формат даты", result)
    }
}