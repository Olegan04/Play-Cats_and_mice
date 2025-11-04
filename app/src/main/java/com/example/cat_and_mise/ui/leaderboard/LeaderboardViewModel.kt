package com.example.cat_and_mise.ui.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cat_and_mise.data.LeaderboardRepository
import com.example.cat_and_mise.data.ScoreRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val repository: LeaderboardRepository
) : ViewModel() {

    val records = repository.getAllRecords()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _topRecords = MutableStateFlow<List<ScoreRecord>>(emptyList())
    val topRecords = _topRecords.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            // Добавляем mock-данные при первом запуске
            repository.addMockRecords()
        }
    }

    fun addNewRecord(playerName: String, score: Int) {
        viewModelScope.launch {
            repository.insertRecord(
                ScoreRecord(
                    playerName = playerName,
                    score = score,
                    date = java.util.Date()
                )
            )
        }
    }

    fun loadTopRecords() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.loadTopRecordsFromServer()

            result.onSuccess { records ->
                _topRecords.value = records
                // Сохраняем загруженные записи в локальную БД
                records.forEach { repository.insertRecord(it) }
            }.onFailure { error ->
                _errorMessage.value = "Ошибка загрузки: ${error.message}"
            }

            _isLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}