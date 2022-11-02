package com.example.ourstory.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.ourstory.core.Sealed
import com.example.ourstory.domain.model.StoryModel
import com.example.ourstory.domain.params.MapParams
import com.example.ourstory.domain.params.StoryParams
import com.example.ourstory.domain.request.AddRequest
import com.example.ourstory.domain.request.LoginRequest
import com.example.ourstory.domain.request.RegisterRequest
import com.example.ourstory.domain.response.GenericResponse
import com.example.ourstory.domain.response.LoginResponse
import com.example.ourstory.domain.response.RegisterResponse
import com.example.ourstory.domain.response.StoriesResponse

interface Repository {
    suspend fun register(req: RegisterRequest): Sealed<RegisterResponse>
    suspend fun login(req: LoginRequest): Sealed<LoginResponse>
    fun getStories(params: StoryParams): LiveData<PagingData<StoryModel>>
    suspend fun getStoriesLocation(params: MapParams): Sealed<StoriesResponse>
    suspend fun addStory(req: AddRequest): Sealed<GenericResponse>
}