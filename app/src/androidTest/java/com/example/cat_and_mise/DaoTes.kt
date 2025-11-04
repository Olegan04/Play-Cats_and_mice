package com.example.cat_and_mise

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cat_and_mise.data.AppDatabase
import com.example.cat_and_mise.data.ScoreRecord
import com.example.cat_and_mise.data.ScoreRecordDao
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class DaoTes {
    private lateinit var database: AppDatabase
    private lateinit var dao: ScoreRecordDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = database.scoreRecordDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetRecords() = runBlocking {
        // Given
        val record = ScoreRecord(
            playerName = "Test Player",
            score = 1000,
            date = Date()
        )

        // When
        dao.insert(record)
        val records = dao.getAllRecords().first()

        // Then
        assertEquals(1, records.size)
        assertEquals("Test Player", records[0].playerName)
        assertEquals(1000, records[0].score)
    }

    @Test
    fun getRecordsSortedByScore() = runBlocking {
        // Given
        val record1 = ScoreRecord(playerName = "Player 1", score = 500, date = Date())
        val record2 = ScoreRecord(playerName = "Player 2", score = 1000, date = Date())

        // When
        dao.insert(record1)
        dao.insert(record2)
        val records = dao.getAllRecords().first()

        // Then
        assertEquals(2, records.size)
        assertEquals("Player 2", records[0].playerName) // Highest score first
        assertEquals("Player 1", records[1].playerName)
    }
}