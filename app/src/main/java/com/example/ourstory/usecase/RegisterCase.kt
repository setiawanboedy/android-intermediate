package com.example.ourstory.usecase

import androidx.lifecycle.LiveData
import com.example.ourstory.core.Sealed
import com.example.ourstory.repository.Repository
import com.example.ourstory.request.RegisterRequest
import com.example.ourstory.response.RegisterResponse
import javax.inject.Inject

class RegisterCase @Inject constructor(
    private val repository: Repository,
) : UseCase<RegisterResponse, RegisterRequest> {
    override fun call(req: RegisterRequest): LiveData<Sealed<RegisterResponse>> =
        repository.register(req)

}