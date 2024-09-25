package com.example.todoapp

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Db.TodoDAO
import com.example.todoapp.ui.theme.MainApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date

class TodoviewModel: ViewModel() {

    val todoDao = MainApplication.todoDatabase.todoDao()

    val todolist: LiveData<List<TODO>> = todoDao.getallTODO()

    fun addTODO(title: String) {

        viewModelScope.launch(Dispatchers.IO)
        {
            todoDao.addTODO(TODO(title = title, createdAt = Date.from(Instant.now())))
        }
    }

    fun deleteTODO(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.deleteTODO(id)
        }
    }
}