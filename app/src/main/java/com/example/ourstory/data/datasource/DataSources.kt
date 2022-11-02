package com.example.ourstory.data.datasource

import com.example.ourstory.core.ErrorParser
import com.example.ourstory.core.SafeResponse
import com.example.ourstory.core.Sealed
import com.example.ourstory.data.network.get.GetService
import com.example.ourstory.data.network.post.AuthService
import com.example.ourstory.data.network.post.PostService
import com.example.ourstory.domain.params.MapParams
import com.example.ourstory.domain.request.AddRequest
import com.example.ourstory.domain.request.LoginRequest
import com.example.ourstory.domain.request.RegisterRequest
import com.example.ourstory.domain.response.GenericResponse
import com.example.ourstory.domain.response.LoginResponse
import com.example.ourstory.domain.response.RegisterResponse
import com.example.ourstory.domain.response.StoriesResponse
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
    suspend fun register(req: RegisterRequest): Sealed<RegisterResponse> =
        safeResponse.enqueue(req, converter::converterGenericError, authService::register)

    suspend fun login(req: LoginRequest): Sealed<LoginResponse> =
        safeResponse.enqueue(req, converter::converterGenericError, authService::login)

    suspend fun getStoriesLocation(params: MapParams): Sealed<StoriesResponse> =
        safeResponse.enqueue(
            params.location,
            converter::converterGenericError,
            getService::getStoriesLocation
        )


    suspend fun addStory(req: AddRequest): Sealed<GenericResponse> {
        val desc = req.description.toRequestBody("text/plain".toMediaType())
        val imageFile = req.file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part =
            MultipartBody.Part.createFormData("photo", req.file.name, imageFile)

        return safeResponse.enqueue(
            desc,
            imageMultipart,
            req.lat,
            req.lon,
            converter::converterGenericError,
            postService::addStory
        )
    }
}