package com.example.ourstory.network.post

import com.example.ourstory.request.LoginRequest
import com.example.ourstory.request.RegisterRequest
import com.example.ourstory.response.LoginResponse
import com.example.ourstory.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}