package com.example.taskot

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskScreen(viewModel: ViewModel, activity: ComponentActivity) {
    val todoList by viewModel.taskList.observeAsState()
    var isDialogOpen by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf(Task(time = Date.from(Instant.now()), title = "")) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .zIndex(1f),
        contentAlignment = Alignment.BottomStart
    ) {
        FloatingActionButton(
            onClick = {
                /*viewModel.exportTasksToFile(activity)*/
            },
            containerColor = Color(0xFFBDFA8F)
        ) {

            Icon(
                painter = painterResource(id = R.drawable.ic_export_data),
                contentDescription = "Export Data",
                tint = Color(0xFF202020)
            )
        }
    }

    Column(modifier = Modifier.fillMaxHeight()) {

            Button(
                onClick = {
                    editingTask = Task(time = Date.from(Instant.now()), title = "")
                    isDialogOpen = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color(0xFF7070fd01)
                )
            ) {
                Text(text = "Add task",
                    color = Color(0xFF202020))
            }
        todoList?.let { list ->
            LazyColumn(content = {
                itemsIndexed(list) { index, item ->
                    TaskItem(
                        item = item,
                        onDelete = { item.id?.let { viewModel.deleteTask(it) } },
                        onUpdate = { task ->
                            editingTask = task
                            isDialogOpen = true
                        }
                    )
                }
            })
        } ?: Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "No items yet",
            fontSize = 16.sp
        )
    }
    if (isDialogOpen) {
        TaskEditDialog(task = editingTask, onDismiss = { isDialogOpen = false }) { newTask ->
            if (newTask.title.isNotBlank()) {
                if (newTask.id == null) {
                    viewModel.addTask(newTask.title)
                } else {
                    viewModel.updateTask(newTask)
                }
                isDialogOpen = false
            }
        }
    }
}

@Composable
fun TaskItem(item : Task, onDelete : ()-> Unit, onUpdate: (Task) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x206c757d))
            .border(1.dp, Color(0x106c757d), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = SimpleDateFormat("HH:mm:aa, dd/mm", Locale.ENGLISH).format(item.time),
                fontSize = 12.sp,
                color = Color(0x806c757d)
            )
            Text(
                text = item.title,
                fontSize = 20.sp,
                color = Color(0xFF202020)
            )
        }
        IconButton(onClick = { onUpdate(item) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "Edit",
                tint = Color(0x806c757d)
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "Delete",
                tint = Color(0x806c757d)
            )
        }
    }
}

@Composable
fun TaskEditDialog(task: Task, onDismiss: () -> Unit, onSave: (Task) -> Unit) {
    var title by remember { mutableStateOf(task.title) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Task content") },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title") }
            )
        },
        confirmButton = {
            Button(
                onClick = { onSave(task.copy(title = title)) },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color(0x7070fd01)
                )
            ) {
                Text("Save", color = Color(0xFF202020))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color(0x7070fd01)
                )
            ) {
                Text("Cancel", color = Color(0xFF202020))
            }
        }
    )
}















