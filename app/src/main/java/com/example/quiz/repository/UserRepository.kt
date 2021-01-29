package com.example.quiz.repository

import com.example.quiz.data.remote.response.GetMessageResponse

interface UserRepository {
    suspend fun login(phone: String, pass: String): GetMessageResponse

}