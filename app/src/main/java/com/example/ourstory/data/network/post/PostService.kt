package com.example.ourstory.data.network.post

import com.example.ourstory.domain.response.GenericResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PostService {
    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part?,
        @Part("lat") lat: Float? = null,
        @Part("lon") lon: Float? = null
    ): Response<GenericResponse>
}