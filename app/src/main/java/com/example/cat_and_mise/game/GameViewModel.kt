package com.example.cat_and_mise.game

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {
    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    fun updateScore(newScore: Int) {
        _score.value = newScore
    }

    fun resetScore() {
        _score.value = 0
    }
}