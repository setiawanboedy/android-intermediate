package com.example.ourstory.utils.service

import com.example.ourstory.data.network.get.GetService
import com.example.ourstory.domain.response.StoriesResponse
import com.example.ourstory.utils.DataDummy
import retrofit2.Response

class FakeGetService : GetService {

    override suspend fun getStories(page: Int, size: Int): Response<StoriesResponse> =
        Response.success(DataDummy.storiesResponseDummy())

    override suspend fun getStoriesLocation(location: Int): Response<StoriesResponse> =
        Response.success(DataDummy.storiesResponseDummy())
}