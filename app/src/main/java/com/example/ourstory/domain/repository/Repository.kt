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
    fun register(req: RegisterRequest): LiveData<Sealed<RegisterResponse>>
    fun login(req: LoginRequest): LiveData<Sealed<LoginResponse>>
    fun getStories(params: StoryParams): LiveData<PagingData<StoryModel>>
    fun getStoriesLocation(params: MapParams): LiveData<Sealed<StoriesResponse>>
    fun addStory(req: AddRequest): LiveData<Sealed<GenericResponse>>
}