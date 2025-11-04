package com.example.cat_and_mise

import com.example.cat_and_mise.ui.level.Level
import com.example.cat_and_mise.ui.level.SharedViewModel
import org.junit.Test
import org.junit.Assert.*

class SharedViewModelTest {
    @Test
    fun testViewModelInitialization() {
        val viewModel = SharedViewModel()

        // Проверяем, что ViewModel создается без ошибок
        assertNotNull(viewModel)
        assertNotNull(viewModel.selectedLevel)
    }

    @Test
    fun testLevelDataClass() {
        val level1 = Level(1, "Level 1", "Легкий")
        val level2 = Level(1, "Level 1", "Легкий")
        val level3 = Level(2, "Level 2", "Сложный")

        // Проверяем equals
        assertEquals(level1, level2)
        assertNotEquals(level1, level3)

        // Проверяем свойства
        assertEquals(1, level1.id)
        assertEquals("Level 1", level1.name)
        assertEquals("Легкий", level1.difficulty)
    }

    @Test
    fun testViewModelMethodsExecuteWithoutErrors() {
        val viewModel = SharedViewModel()
        val testLevel = Level(1, "Test Level", "Легкий")

        // Теперь с postValue эти методы не должны бросать исключения
        viewModel.selectLevel(testLevel)
        viewModel.clearSelection()

        // Если дошли сюда без исключений - тест пройден
        assertTrue(true)
    }

    @Test
    fun testLevelCreationWithDifferentParameters() {
        val levels = listOf(
            Level(1, "Level 1", "Легкий"),
            Level(2, "Level 2", "Средний"),
            Level(3, "Level 3", "Сложный")
        )

        for (level in levels) {
            assertNotNull(level)
            assertTrue(level.id >= 0)
            assertNotNull(level.name)
            assertNotNull(level.difficulty)
        }
    }
}