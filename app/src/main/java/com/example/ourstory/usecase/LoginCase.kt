package com.example.ourstory.usecase

import androidx.lifecycle.LiveData
import com.example.ourstory.core.Sealed
import com.example.ourstory.repository.Repository
import com.example.ourstory.request.LoginRequest
import com.example.ourstory.response.LoginResponse
import javax.inject.Inject

class LoginCase @Inject constructor(
    private val repository: Repository,
) : UseCase<LoginResponse, LoginRequest>{
    override fun call(req: LoginRequest): LiveData<Sealed<LoginResponse>> = repository.login(req)

}