package com.example.cat_and_mise.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreRecordDao {
    @Insert
    suspend fun insert(scoreRecord: ScoreRecord)

    @Query("SELECT * FROM score_records ORDER BY score DESC")
    fun getAllRecords(): Flow<List<ScoreRecord>>

    @Query("SELECT * FROM score_records ORDER BY score DESC")
    suspend fun getAllRecordsSync(): List<ScoreRecord>

    @Query("SELECT * FROM score_records ORDER BY score DESC LIMIT :limit")
    fun getTopRecords(limit: Int): Flow<List<ScoreRecord>>

    @Query("DELETE FROM score_records")
    suspend fun clearAll()
}