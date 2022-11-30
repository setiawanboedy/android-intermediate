package com.example.ourstory.utils.service

import com.example.ourstory.data.network.post.AuthService
import com.example.ourstory.domain.request.LoginRequest
import com.example.ourstory.domain.request.RegisterRequest
import com.example.ourstory.domain.response.LoginResponse
import com.example.ourstory.domain.response.RegisterResponse
import com.example.ourstory.utils.DataDummy
import retrofit2.Response

class FakeAuthService : AuthService {
    override suspend fun register(request: RegisterRequest): Response<RegisterResponse> =
        Response.success(DataDummy.registerResponseDummy())

    override suspend fun login(request: LoginRequest): Response<LoginResponse> =
        Response.success(DataDummy.loginResponseDummy())
}