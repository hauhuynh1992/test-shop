package com.example.quiz.data.local.pref

import android.content.Context
import com.google.gson.Gson

class AppPrefs(
    context: Context,
    val gson: Gson
) : PrefHelper {

    companion object {
        private const val TOKEN = "TOKEN"
    }

    private val sharedPreferences = context.getSharedPreferences(
        context.packageName,
        Context.MODE_PRIVATE
    )
}