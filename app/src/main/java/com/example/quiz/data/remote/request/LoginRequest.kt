package com.example.quiz.data.remote.request

import com.google.gson.annotations.SerializedName

class LoginRequest(
    @field:SerializedName("mobile_number") var mobile_number: String,
    @field:SerializedName("password") var password: String
)