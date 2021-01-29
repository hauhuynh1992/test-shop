package com.example.quiz.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quiz.data.local.dao.UserDao
import com.example.quiz.model.User

@Database(
    entities = [
        User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}