package com.example.quiz.data.remote

import com.example.quiz.data.remote.request.LoginRequest
import com.example.quiz.data.remote.response.GetMessageResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/v1/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): GetMessageResponse
}