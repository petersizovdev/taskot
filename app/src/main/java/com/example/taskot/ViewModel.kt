package com.example.taskot

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class ViewModel : ViewModel() {
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



    /*@RequiresApi(Build.VERSION_CODES.O)
    fun exportTasksToFile() {

    }*/
}

