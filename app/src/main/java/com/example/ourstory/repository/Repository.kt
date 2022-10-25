package com.example.ourstory.repository

import androidx.lifecycle.LiveData
import com.example.ourstory.core.Sealed
import com.example.ourstory.params.StoryParams
import com.example.ourstory.request.AddRequest
import com.example.ourstory.request.LoginRequest
import com.example.ourstory.request.RegisterRequest
import com.example.ourstory.response.GenericResponse
import com.example.ourstory.response.LoginResponse
import com.example.ourstory.response.RegisterResponse
import com.example.ourstory.response.StoriesResponse

interface Repository {
    fun register(req: RegisterRequest): LiveData<Sealed<RegisterResponse>>
    fun login(req: LoginRequest): LiveData<Sealed<LoginResponse>>
    fun getStories(params: StoryParams): LiveData<Sealed<StoriesResponse>>
    fun addStory(req: AddRequest): LiveData<Sealed<GenericResponse>>
}