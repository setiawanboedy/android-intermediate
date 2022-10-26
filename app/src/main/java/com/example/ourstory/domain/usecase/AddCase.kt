package com.example.ourstory.domain.usecase

import androidx.lifecycle.LiveData
import com.example.ourstory.core.Sealed
import com.example.ourstory.domain.repository.Repository
import com.example.ourstory.domain.request.AddRequest
import com.example.ourstory.domain.response.GenericResponse
import javax.inject.Inject

class AddCase @Inject constructor(
    private val repository: Repository,
) : UseCase<GenericResponse, AddRequest> {
    override fun call(req: AddRequest): LiveData<Sealed<GenericResponse>> = repository.addStory(req)

}