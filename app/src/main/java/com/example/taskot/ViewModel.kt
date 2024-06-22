package com.example.taskot

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room.databaseBuilder
import com.example.taskot.db.Dao
import com.opencsv.CSVWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.time.Instant
import java.util.Date


class ViewModel (private val dao: Dao) : ViewModel() {
    val taskDao = App.taskDb.getDao()
    val taskList : LiveData<List<Task>> = taskDao.getAllTasks()

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTask(title : String){
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.addTask(Task(title = title, time = Date.from(Instant.now())))
        }
    }
    fun deleteTask(id : Int){
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.deleteTask(id)
        }
    }
    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.updateTask(task)
        }
    }

    fun exportDatabaseToCSV(activity: ComponentActivity) {
        viewModelScope.launch(Dispatchers.IO) {
            val context = activity.applicationContext
            val tasks = dao.getAllTasksList()
            exportToCSV(tasks, context)
        }
    }

    private fun exportToCSV(tasks: List<Task>, context: Context) {
        val exportDir = File(context.getExternalFilesDir(null), "exports")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "tasks.csv")
        try {
            file.createNewFile()
            CSVWriter(FileWriter(file)).use { csvWrite ->
                csvWrite.writeNext(arrayOf("ID", "Title", "Time"))
                for (task in tasks) {
                    val arrStr = arrayOf(task.id.toString(), task.title, task.time.toString())
                    csvWrite.writeNext(arrStr)
                }
            }
        } catch (sqlEx: Exception) {
            Log.e("ViewModel", sqlEx.message, sqlEx)
        }
    }
}

