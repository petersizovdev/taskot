package com.example.taskot.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskot.Task

@Dao
interface Dao {

    @Query("SELECT * FROM Task ORDER BY time DESC")
    fun getAllTasks() : LiveData<List<Task>>

    @Query("SELECT * FROM Task")
    fun getAllTasksList(): List<Task>

    @Insert
    fun addTask(task : Task)

    @Update
    fun updateTask(task: Task)

    @Query("Delete FROM Task where id = :id")
    fun deleteTask(id : Int)
}