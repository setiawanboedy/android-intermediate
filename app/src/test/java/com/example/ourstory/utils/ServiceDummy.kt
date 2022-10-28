package com.example.ourstory.utils

import com.example.ourstory.data.network.get.GetService
import com.example.ourstory.data.network.post.AuthService
import com.example.ourstory.data.network.post.PostService
import com.example.ourstory.domain.request.LoginRequest
import com.example.ourstory.domain.request.RegisterRequest
import com.example.ourstory.domain.response.GenericResponse
import com.example.ourstory.domain.response.LoginResponse
import com.example.ourstory.domain.response.RegisterResponse
import com.example.ourstory.domain.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class ServiceDummy : AuthService, PostService, GetService {
    override suspend fun register(request: RegisterRequest): Response<RegisterResponse> =
        Response.success(DataDummy.registerResponseDummy())

    override suspend fun login(request: LoginRequest): Response<LoginResponse> =
        Response.success(DataDummy.loginResponseDummy())

    override suspend fun addStory(
        description: RequestBody,
        file: MultipartBody.Part?,
        lat: Float?,
        lon: Float?
    ): Response<GenericResponse> =
        Response.success(DataDummy.addStoryResponseDummy())

    override suspend fun getStories(page: Int, size: Int): Response<StoriesResponse> =
        Response.success(DataDummy.storiesResponseDummy())

    override suspend fun getStoriesLocation(location: Int): Response<StoriesResponse> =
        Response.success(DataDummy.storiesResponseDummy())

    fun registerError(): RegisterResponse =
        DataDummy.registerErrorDummy()

    fun loginError(): GenericResponse =
        DataDummy.loginErrorDummy()

    fun uploadError(): GenericResponse =
        DataDummy.uploadErrorDummy()
}