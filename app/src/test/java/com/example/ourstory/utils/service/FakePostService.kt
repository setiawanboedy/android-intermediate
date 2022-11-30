package com.example.ourstory.utils.service

import com.example.ourstory.data.network.post.PostService
import com.example.ourstory.domain.response.GenericResponse
import com.example.ourstory.utils.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class FakePostService : PostService {

    override suspend fun addStory(
        description: RequestBody,
        file: MultipartBody.Part?,
        lat: Float?,
        lon: Float?
    ): Response<GenericResponse> =
        Response.success(DataDummy.addStoryResponseDummy())
}