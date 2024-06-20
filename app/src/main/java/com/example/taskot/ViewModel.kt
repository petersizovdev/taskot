package com.example.taskot

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
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


}