package com.example.ourstory.domain.usecase

import com.example.ourstory.core.Sealed
import com.example.ourstory.domain.repository.Repository
import com.example.ourstory.domain.request.RegisterRequest
import com.example.ourstory.domain.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RegisterCase @Inject constructor(
    private val repository: Repository,
) : UseCase<RegisterResponse, RegisterRequest> {
    override fun call(req: RegisterRequest): Flow<Sealed<RegisterResponse>> = flow {
        emit(Sealed.loading(null))

        val res = repository.register(req)
        emit(res)
    }.flowOn(Dispatchers.IO)

}