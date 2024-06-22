package com.example.taskot

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.taskot.ui.theme.TaskotTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.taskot.db.ViewModelFactory

// Ваш MainActivity с использованием ViewModelFactory
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создаем экземпляр Dao
        val dao = App.taskDb.getDao()

        // Создаем фабрику ViewModel с зависимостью Dao
        val viewModelFactory = ViewModelFactory(dao)

        // Получаем ViewModel через фабрику
        val viewModel = ViewModelProvider(this, viewModelFactory)[ViewModel::class.java]
        setContent {
            TaskotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskScreen(viewModel, this)
                }
            }
        }
    }
}
