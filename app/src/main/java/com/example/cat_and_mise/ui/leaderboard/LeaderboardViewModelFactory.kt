package com.example.cat_and_mise.ui.leaderboard

import com.example.cat_and_mise.data.LeaderboardRepository

class LeaderboardViewModelFactory (
    private val repository: LeaderboardRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeaderboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeaderboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}