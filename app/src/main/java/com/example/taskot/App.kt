package com.example.taskot

import android.app.Application
import androidx.room.Room
import com.example.taskot.db.Db

class App : Application() {

    companion object {
        lateinit var taskDb: Db
    }

    override fun onCreate() {
        super.onCreate()
        taskDb = Room.databaseBuilder(
            applicationContext,
            Db::class.java,
            Db.NAME
        ).build()
    }
}