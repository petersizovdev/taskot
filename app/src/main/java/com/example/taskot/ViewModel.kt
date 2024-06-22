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
import com.example.taskot.db.Helpers
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale


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

    fun importDatabaseFromCSV(activity: ComponentActivity) {
        viewModelScope.launch(Dispatchers.IO) {
            val context = activity.applicationContext
            val csvFile = File(context.getExternalFilesDir(null), "exports/tasks.csv")
            if (csvFile.exists()) {
                CSVReader(FileReader(csvFile)).use { csvReader ->
                    // Пропускаем заголовок CSV
                    csvReader.readNext()

                    var nextLine: Array<String>?
                    while (csvReader.readNext().also { nextLine = it } != null) {
                        if (nextLine != null && nextLine!!.size >= 3) {
                            val id = nextLine!![0].toIntOrNull()
                            val title = nextLine!![1]
                            try {
                                // Обновляем формат для соответствия вашему формату даты и времени
                                val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                                val time = dateFormat.parse(nextLine!![2])
                                if (id != null && time != null) {
                                    val task = Task(id, title, time)
                                    dao.addTask(task)
                                }
                            } catch (e: ParseException) {
                                // Обработка ошибки, если строка не может быть преобразована в дату
                                Log.e("ViewModel", "Unparseable date: ${nextLine!![2]}", e)
                            }
                        }
                    }
                }
            }
        }
    }
}
