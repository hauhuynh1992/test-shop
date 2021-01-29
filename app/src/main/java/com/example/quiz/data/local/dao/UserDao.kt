package com.example.quiz.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quiz.model.User

interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getUsers(): List<User>?

    @Query("SELECT * FROM user WHERE user.id = :id")
    suspend fun getUser(id: String): User?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(job: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(list: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteUser(id: String)

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Query("SELECT * FROM user LIMIT :pageSize OFFSET :pageIndex")
    suspend fun getUserPage(pageSize: Int, pageIndex: Int): List<User>?
}