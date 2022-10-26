package com.example.ourstory.domain.usecase

import androidx.lifecycle.LiveData
import com.example.ourstory.core.Sealed
import com.example.ourstory.domain.repository.Repository
import com.example.ourstory.domain.request.LoginRequest
import com.example.ourstory.domain.response.LoginResponse
import javax.inject.Inject

class LoginCase @Inject constructor(
    private val repository: Repository,
) : UseCase<LoginResponse, LoginRequest> {
    override fun call(req: LoginRequest): LiveData<Sealed<LoginResponse>> = repository.login(req)

}