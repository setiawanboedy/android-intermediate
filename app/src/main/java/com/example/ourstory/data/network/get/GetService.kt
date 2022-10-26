package com.example.ourstory.data.network.get

import com.example.ourstory.domain.response.StoriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GetService {
    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<StoriesResponse>

    @GET("stories")
    suspend fun getStoriesLocation(
        @Query("location") location: Int,
    ): Response<StoriesResponse>
}