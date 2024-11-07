package com.example.todoapp

import android.R.id.message
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TodoListPage(viewModel: TodoviewModel) {
    val todoList by viewModel.todolist.observeAsState()
    var inputTxt by remember { mutableStateOf("") }
    var timerMinutes by remember { mutableStateOf("") }
    var showEmptyInputAlert by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var showTimerAlert by remember { mutableStateOf(false) }
    var todoToDelete by remember { mutableStateOf<TODO?>(null) }
    val scope = rememberCoroutineScope()

    // Timer Alert Dialog
    if (showTimerAlert) {
        AlertDialog(
            onDismissRequest = { showTimerAlert = false },
            title = { Text("Timer Complete!") },
            text = { Text("Time to do your thing!") },
            confirmButton = {
                TextButton(onClick = { showTimerAlert = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Empty Input Alert Dialog
    if (showEmptyInputAlert) {
        AlertDialog(
            onDismissRequest = { showEmptyInputAlert = false },
            title = { Text("Error") },
            text = { Text("Please enter a task before adding") },
            confirmButton = {
                TextButton(onClick = { showEmptyInputAlert = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        todoToDelete?.let { viewModel.deleteTODO(it.id) }
                        showDeleteConfirmation = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }


    // Timer function
    fun startTimer(minutes: Int) {
        scope.launch {
            delay(minutes * 60 * 1000L) // Convert minutes to milliseconds
            showTimerAlert = true
        }
    }

    // Composable content
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(8.dp)
    ) {
        // Input Row (Text Field + Timer + Button)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputTxt,
                onValueChange = { inputTxt = it },
                placeholder = { Text("Enter task") },
                modifier = Modifier.weight(2f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = timerMinutes,
                onValueChange = {
                    // Only allow numeric input
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        timerMinutes = it
                    }
                },
                placeholder = { Text("Minutes") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (inputTxt.isBlank()) {
                        showEmptyInputAlert = true
                    } else {
                        viewModel.addTODO(inputTxt)
                        if (timerMinutes.isNotEmpty()) {
                            startTimer(timerMinutes.toInt())
                        }
                        inputTxt = ""
                        timerMinutes = ""
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Add")
            }
        }

        // Show list of Todo items
        todoList?.let {
            if (it.isEmpty()) {
                Text(
                    text = "No items yet",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn {
                    itemsIndexed(it) { index: Int, item: TODO ->
                        TODOitem(
                            item = item,
                            onDelete = {
                                todoToDelete = item
                                showDeleteConfirmation = true
                            }
                        )
                    }
                }
            }
        } ?: Text(
            text = "No items yet",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )


        }
    }

@Composable
fun TODOitem(item: TODO, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = SimpleDateFormat(
                    "HH:mm:aa,dd/MM/yyyy",
                    Locale.ENGLISH
                ).format(item.createdAt),
                fontSize = 12.sp,
                color = Color.DarkGray
            )
            Text(
                text = item.title,
                fontSize = 20.sp,
                color = Color.Black
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_delete_24),
                contentDescription = "Delete",
                tint = Color.Black
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    TodoListPage(viewModel = TodoviewModel())
}