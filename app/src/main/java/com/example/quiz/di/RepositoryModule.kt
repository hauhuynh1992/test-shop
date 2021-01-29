package com.example.quiz.di

import android.content.Context
import androidx.room.Room
import com.example.quiz.AppDispatchers
import com.example.quiz.data.constants.Constants
import com.example.quiz.data.local.db.AppDatabase
import com.example.quiz.data.local.pref.AppPrefs
import com.example.quiz.data.local.pref.PrefHelper
import com.example.quiz.repository.UserRepository
import com.example.quiz.repository.iml.UserRepositoryImpl
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val repositoryModule = module {
    factory { AppDispatchers(Dispatchers.Main, Dispatchers.IO) }
    single { createDatabaseName() }
    single { createAppDatabase(get(), get()) }
    single { createUserDao(get()) }
    single<PrefHelper> { AppPrefs(get(), get()) }
    single { Gson() }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
}

fun createDatabaseName() = Constants.DATABASE_NAME

fun createAppDatabase(dbName: String, context: Context) =
    Room.databaseBuilder(context, AppDatabase::class.java, dbName).build()

fun createUserDao(appDatabase: AppDatabase) = appDatabase.userDao()