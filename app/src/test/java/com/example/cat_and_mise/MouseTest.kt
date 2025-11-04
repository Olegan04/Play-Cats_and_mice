package com.example.cat_and_mise

import com.example.cat_and_mise.game_objects.Mouse
import org.junit.Assert.assertEquals
import org.junit.Test

class MouseTest {
    @Test
    fun testGetMouseInfo_WithDefaultValues() {
        val mouse = Mouse()
        val expected = "Мышь по имени  бежит со скоростью 0"
        assertEquals(expected, mouse.getMouseInfo())
    }

    @Test
    fun testGetMouseInfo_WithCustomValues() {
        val mouse = Mouse("Джерри", 10)
        val expected = "Мышь по имени Джерри бежит со скоростью 10"
        assertEquals(expected, mouse.getMouseInfo())
    }

    @Test
    fun testGetMouseInfo_WithEmptyName() {
        val mouse = Mouse("", 5)
        val expected = "Мышь по имени  бежит со скоростью 5"
        assertEquals(expected, mouse.getMouseInfo())
    }

    @Test
    fun testGetMouseInfo_WithFullValues() {
        val mouse = Mouse("Джерри", 5)
        val expected = "Мышь по имени Джерри бежит со скоростью 5"
        assertEquals(expected, mouse.getMouseInfo())
    }
}