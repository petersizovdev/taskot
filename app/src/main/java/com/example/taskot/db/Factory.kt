package com.example.taskot.db

import androidx.lifecycle.ViewModelProvider
import com.example.taskot.ViewModel
import com.example.taskot.db.Dao

class ViewModelFactory(private val dao: Dao) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}