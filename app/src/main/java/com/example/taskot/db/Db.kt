package com.example.taskot.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskot.Task

@Database(entities = [Task::class], version = 1)
@TypeConverters(Helpers::class)
abstract class Db : RoomDatabase(){

    companion object {
        const val NAME = "TaskDB"
    }

    abstract fun getDao() : Dao

}