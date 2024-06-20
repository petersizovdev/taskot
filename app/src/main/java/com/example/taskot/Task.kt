package com.example.taskot

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Task (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val time: Date
)