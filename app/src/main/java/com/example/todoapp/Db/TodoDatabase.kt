package com.example.todoapp.Db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoapp.TODO

@Database(entities = [TODO::class], version = 1)
@TypeConverters(Converters::class)

abstract class TodoDatabase: RoomDatabase() {
    companion object{
        const val NAME = "todo_db"

    }
    abstract fun todoDao(): TodoDAO
}