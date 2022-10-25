package com.example.ourstory.datasource

import com.example.ourstory.core.ErrorParser
import com.example.ourstory.core.SafeResponse
import com.example.ourstory.core.Sealed
import com.example.ourstory.network.get.GetService
import com.example.ourstory.network.post.AuthService
import com.example.ourstory.network.post.PostService
import com.example.ourstory.params.StoryParams
import com.example.ourstory.request.AddRequest
import com.example.ourstory.request.LoginRequest
import com.example.ourstory.request.RegisterRequest
import com.example.ourstory.response.GenericResponse
import com.example.ourstory.response.LoginResponse
import com.example.ourstory.response.RegisterResponse
import com.example.ourstory.response.StoriesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class DataSources @Inject constructor(
    private val safeResponse: SafeResponse,
    private val converter: ErrorParser,
    private val authService: AuthService,
    private val getService: GetService,
    private val postService: PostService,
) {
    fun register(req: RegisterRequest): Flow<Sealed<RegisterResponse>> = flow {
        emit(Sealed.loading(null))

        val res = safeResponse.enqueue(req, converter::converterGenericError, authService::register)
        emit(res)
    }.flowOn(Dispatchers.IO)

    fun login(req: LoginRequest): Flow<Sealed<LoginResponse>> = flow {
        emit(Sealed.loading(null))

        val res = safeResponse.enqueue(req, converter::converterGenericError, authService::login)
        emit(res)
    }.flowOn(Dispatchers.IO)

    fun getStories(params: StoryParams): Flow<Sealed<StoriesResponse>> = flow {
        emit(Sealed.loading(null))

        val res = safeResponse.enqueue(
            params.page,
            params.size,
            converter::converterGenericError,
            getService::getStories
        )
        emit(res)
    }.flowOn(Dispatchers.IO)

    fun addStory(req: AddRequest): Flow<Sealed<GenericResponse>> = flow {
        emit(Sealed.loading(null))

        val desc = req.description.toRequestBody("text/plain".toMediaType())
        val imageFile = req.file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part =
            MultipartBody.Part.createFormData("photo", req.file.name, imageFile)

        val res = safeResponse.enqueue(
            desc,
            imageMultipart,
            req.lat,
            req.lon,
            converter::converterGenericError,
            postService::addStory
        )
        emit(res)
    }.flowOn(Dispatchers.IO)
}