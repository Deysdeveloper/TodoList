package com.example.todoapp.ui.theme

import android.app.Application
import androidx.room.Room
import com.example.todoapp.Db.TodoDatabase

class MainApplication: Application() {
    companion object{
        lateinit var todoDatabase:TodoDatabase
            private set
    }
    override fun onCreate() {
        super.onCreate()
        todoDatabase = Room.databaseBuilder(applicationContext,
            TodoDatabase::class.java,
            TodoDatabase.NAME).build()
    }
}