package com.example.cat_and_mise.data

import kotlinx.coroutines.flow.Flow

class LeaderboardRepository(
    private val scoreRecordDao: ScoreRecordDao,
    private val leaderboardApi: LeaderboardApi
) {

    fun getAllRecords(): Flow<List<ScoreRecord>> {
        return scoreRecordDao.getAllRecords()
    }

    suspend fun insertRecord(scoreRecord: ScoreRecord) {
        val existingRecords = scoreRecordDao.getAllRecordsSync()
        val isDuplicate = existingRecords.any {
            it.playerName == scoreRecord.playerName && it.score == scoreRecord.score
        }

        if (!isDuplicate) {
            scoreRecordDao.insert(scoreRecord)
        }
    }

    suspend fun loadTopRecordsFromServer(): Result<List<ScoreRecord>> {
        return try {
            val remoteRecords = leaderboardApi.getTopRecords()
            val scoreRecords = remoteRecords.take(5).map { it.toScoreRecord() }
            Result.success(scoreRecords)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addMockRecords() {
        val mockRecords = listOf(
            ScoreRecord(playerName = "Кот Леопольд", score = 1500, date = java.util.Date()),
            ScoreRecord(playerName = "Мышь Джерри", score = 1200, date = java.util.Date()),
            ScoreRecord(playerName = "Кот Том", score = 900, date = java.util.Date()),
            ScoreRecord(playerName = "Мышь Микки", score = 800, date = java.util.Date()),
            ScoreRecord(playerName = "Кот Гарфилд", score = 600, date = java.util.Date())
        )

        mockRecords.forEach { scoreRecordDao.insert(it) }
    }
}