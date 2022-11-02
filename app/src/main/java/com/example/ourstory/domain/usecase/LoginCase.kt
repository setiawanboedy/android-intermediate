package com.example.ourstory.domain.usecase

import com.example.ourstory.core.Sealed
import com.example.ourstory.domain.repository.Repository
import com.example.ourstory.domain.request.LoginRequest
import com.example.ourstory.domain.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginCase @Inject constructor(
    private val repository: Repository,
) : UseCase<LoginResponse, LoginRequest> {
    override fun call(req: LoginRequest): Flow<Sealed<LoginResponse>> = flow {
        emit(Sealed.loading(null))
        val res = repository.login(req)
        emit(res)
    }.flowOn(Dispatchers.IO)

}