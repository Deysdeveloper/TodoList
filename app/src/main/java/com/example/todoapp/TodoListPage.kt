package com.example.todoapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TodoListPage(viewModel:TodoviewModel)
{
    val todoList by viewModel.todolist.observeAsState()
    var inputTxt by remember { mutableStateOf("") }
    Column(modifier = Modifier
        .fillMaxHeight()
        .padding(8.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            OutlinedTextField(value = inputTxt, onValueChange = {inputTxt=it})
            Button(onClick = {
                viewModel.addTODO(inputTxt)
                inputTxt=""
            })
            {
                Text(text = "Add")
            }
        }
        todoList?.let {
            LazyColumn(
                content = {
                    itemsIndexed(it){index: Int, item: TODO ->
                        TODOitem(item, onDelete = {
                            viewModel.deleteTODO(item.id)
                        })
                    }
                }
            )

        }?: Text(text = "No items yet",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 20.sp,
            textAlign = TextAlign.Center)


    }
}
@Composable
fun TODOitem(item:TODO,onDelete:() -> Unit)
{
    Row(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.primaryContainer)
        .padding(16.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically)
    {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = SimpleDateFormat("HH:mm:aa,dd/MM/yyyy", Locale.ENGLISH).format(item.createdAt),
                fontSize = 12.sp,
                color= Color.DarkGray)
            Text(text = item.title,
                fontSize = 20.sp,
                color= Color.Black)
        }
        IconButton(onClick =onDelete) {
            Icon(painter =painterResource(id = R.drawable.baseline_delete_24) ,
                contentDescription ="Delete",
                tint = Color.Black)

        }

    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
        TodoListPage(TodoviewModel())
    }