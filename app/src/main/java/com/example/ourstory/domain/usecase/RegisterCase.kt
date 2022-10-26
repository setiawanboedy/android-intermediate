package com.example.ourstory.domain.usecase

import androidx.lifecycle.LiveData
import com.example.ourstory.core.Sealed
import com.example.ourstory.domain.repository.Repository
import com.example.ourstory.domain.request.RegisterRequest
import com.example.ourstory.domain.response.RegisterResponse
import javax.inject.Inject

class RegisterCase @Inject constructor(
    private val repository: Repository,
) : UseCase<RegisterResponse, RegisterRequest> {
    override fun call(req: RegisterRequest): LiveData<Sealed<RegisterResponse>> =
        repository.register(req)

}