package com.example.cat_and_mise.ui.level

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _selectedLevel = MutableLiveData<Level?>()
    val selectedLevel: LiveData<Level?> = _selectedLevel

    fun selectLevel(level: Level) {
        _selectedLevel.postValue(level)
    }

    fun clearSelection() {
        _selectedLevel.postValue(null)
    }
}