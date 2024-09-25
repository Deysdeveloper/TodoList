package com.example.todoapp.Db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todoapp.TODO

@Dao
interface TodoDAO {
    @Query("SELECT * FROM TODO ORDER BY createdAt DESC")
    fun getallTODO(): LiveData<List<TODO>>
    @Insert
    fun addTODO(todo: TODO)
    @Query("DELETE FROM TODO WHERE id = :id")
    fun deleteTODO(id: Int)


    }