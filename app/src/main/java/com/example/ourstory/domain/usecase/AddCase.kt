package com.example.ourstory.domain.usecase

import com.example.ourstory.core.Sealed
import com.example.ourstory.domain.repository.Repository
import com.example.ourstory.domain.request.AddRequest
import com.example.ourstory.domain.response.GenericResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddCase @Inject constructor(
    private val repository: Repository,
) : UseCase<GenericResponse, AddRequest> {
    override fun call(req: AddRequest): Flow<Sealed<GenericResponse>> = flow {
        emit(Sealed.loading(null))
        val res = repository.addStory(req)
        emit(res)
    }.flowOn(Dispatchers.IO)

}