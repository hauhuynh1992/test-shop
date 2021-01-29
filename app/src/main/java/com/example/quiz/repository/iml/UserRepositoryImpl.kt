package com.example.quiz.repository.iml

import com.example.quiz.data.local.dao.UserDao
import com.example.quiz.data.remote.ApiService
import com.example.quiz.data.remote.request.LoginRequest
import com.example.quiz.data.remote.response.GetMessageResponse
import com.example.quiz.repository.UserRepository

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun login(phone: String, pass: String): GetMessageResponse {
        return apiService.login(LoginRequest(phone, pass))
    }

}